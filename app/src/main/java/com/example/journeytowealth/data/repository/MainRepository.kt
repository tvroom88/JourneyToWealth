package com.example.journeytowealth.data.repository

import android.util.Log
import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.core.utils.ExcelParser
import com.example.journeytowealth.data.local.MarketIndexLocalDataSource
import com.example.journeytowealth.data.local.StockLocalDataSource
import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import com.example.journeytowealth.data.local.entity.StockEntity
import com.example.journeytowealth.data.mapper.toEntity
import com.example.journeytowealth.data.model.DbData
import com.example.journeytowealth.data.model.ExcelData
import com.example.journeytowealth.data.remote.ExcelRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class MainRepository(
    private val excelRemoteDataSource: ExcelRemoteDataSource,
    private val stockLocalDataSource: StockLocalDataSource,
    private val marketIndexLocalDataSource: MarketIndexLocalDataSource
) {

    /**
     * Excel 다운로드 후 DTO 변환
     */
    suspend fun downloadExcel(accessToken: String): HttpResult<ByteArray> {
        return excelRemoteDataSource.downloadGoogleSheet(accessToken)
    }

    suspend fun insertExcelToDb(excelBytes: ByteArray): HttpResult<Unit> {
        return try {
            val excelData = ExcelParser.parse(excelBytes)

            val stockEntities = excelData.stocks.map { it.toEntity() }
            val marketIndexEntities = excelData.indexes.map { it.toEntity() }

            // DB 저장
            stockLocalDataSource.insertStocks(stockEntities)
            marketIndexLocalDataSource.insertMarketIndexes(marketIndexEntities)

            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, "Failed to parse or save Excel")
        }
    }

    // DB Flow
    fun observeDb(): Flow<DbData> = combine(
        stockLocalDataSource.getStocks(),
        marketIndexLocalDataSource.getMarketIndexes()
    ) { stocks, indexes ->
        DbData(
            stocks = stocks,
            indexes = indexes
        )
    }


    // 4️⃣ 통합 Refresh
    suspend fun refreshExcelData(accessToken: String): HttpResult<Unit> {
        return when (val downloadResult = downloadExcel(accessToken)) {
            is HttpResult.Success -> insertExcelToDb(downloadResult.data)
            is HttpResult.Error -> HttpResult.Error(downloadResult.exception, downloadResult.message)
            is HttpResult.Loading -> HttpResult.Loading
        }
    }
}