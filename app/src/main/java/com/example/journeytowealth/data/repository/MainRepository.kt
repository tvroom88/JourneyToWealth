package com.example.journeytowealth.data.repository

import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.data.local.MarketIndexLocalDataSource
import com.example.journeytowealth.data.local.PortfolioLocalDataSource
import com.example.journeytowealth.data.local.StockLocalDataSource
import com.example.journeytowealth.data.mapper.toEntity
import com.example.journeytowealth.data.mapper.toSectionUiModel
import com.example.journeytowealth.data.model.DbData
import com.example.journeytowealth.data.model.ExcelData
import com.example.journeytowealth.data.remote.ExcelRemoteDataSource
import com.example.journeytowealth.ui.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class MainRepository(
    private val excelRemoteDataSource: ExcelRemoteDataSource,
    private val stockLocalDataSource: StockLocalDataSource,
    private val marketIndexLocalDataSource: MarketIndexLocalDataSource,
    private val portfolioLocalDataSource: PortfolioLocalDataSource

) {

    /**
     * Excel 다운로드 후 DTO 변환
     */
    suspend fun downloadExcel(accessToken: String): HttpResult<ByteArray> {
        return excelRemoteDataSource.downloadGoogleSheet(accessToken)
    }

    suspend fun insertExcelToDb(excelData: ExcelData): Result<Unit> {
        return try {

            val stockEntities = excelData.stocks.map { it.toEntity() }
            val marketIndexEntities = excelData.indexes.map { it.toEntity() }
            val portfolioEntities = excelData.portfolios.map { it.toEntity() }

            // DB 저장
            stockLocalDataSource.insertStocks(stockEntities)
            marketIndexLocalDataSource.insertMarketIndexes(marketIndexEntities)
            portfolioLocalDataSource.insertPortfolios(portfolioEntities)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // DB Flow -> Fragment에서 구독해서 Db 내용을 가져간다.
    fun observeDb(): Flow<UiState<DbData>> =
        combine(
            stockLocalDataSource.getStocks(),
            marketIndexLocalDataSource.getMarketIndexes(),
            portfolioLocalDataSource.getPortfolios()
        ) { stocks, indexes, portfolios ->

            val portfolioSectionUiMode = portfolios.toSectionUiModel()
            DbData(indexes, stocks, portfolioSectionUiMode)
        }
            .map<DbData, UiState<DbData>> {
                UiState.Success(it)
            }
            .onStart {
                emit(UiState.Loading)
            }
            .catch { e ->
                emit(UiState.Error(e.message ?: "Unknown error", e))
            }

}