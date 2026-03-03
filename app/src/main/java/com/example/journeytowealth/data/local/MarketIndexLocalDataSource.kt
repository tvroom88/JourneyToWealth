package com.example.journeytowealth.data.local

import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.data.local.dao.MarketIndexDao
import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import kotlinx.coroutines.flow.Flow

class MarketIndexLocalDataSource(
    private val marketIndexDao: MarketIndexDao
) {

    fun getMarketIndexes(): Flow<List<MarketIndexEntity>> {
        return marketIndexDao.getAll()
    }

    suspend fun insertMarketIndexes(
        list: List<MarketIndexEntity>
    ): HttpResult<Unit> {
        return try {
            marketIndexDao.insertAll(list)
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }

    suspend fun clearMarketIndexes(): HttpResult<Unit> {
        return try {
            marketIndexDao.deleteAll()
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }
}