package com.example.journeytowealth.data.local

import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.data.local.dao.StockDao
import com.example.journeytowealth.data.local.entity.StockEntity
import kotlinx.coroutines.flow.Flow

class StockLocalDataSource(
    private val stockDao: StockDao
) {

    fun getStocks(): Flow<List<StockEntity>> {
        return stockDao.getAll()
    }

    suspend fun insertStocks(stocks: List<StockEntity>): HttpResult<Unit> {
        return try {
            stockDao.insertAll(stocks)
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }

    suspend fun clearStocks(): HttpResult<Unit> {
        return try {
            stockDao.deleteAll()
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }

}