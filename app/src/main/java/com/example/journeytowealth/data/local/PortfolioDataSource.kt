package com.example.journeytowealth.data.local

import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.data.local.dao.PortfolioDao
import com.example.journeytowealth.data.local.entity.PortfolioEntity
import kotlinx.coroutines.flow.Flow

class PortfolioLocalDataSource(
    private val portfolioDao: PortfolioDao
) {

    fun getPortfolios(): Flow<List<PortfolioEntity>> {
        return portfolioDao.getAll()
    }

    suspend fun insertPortfolios(
        list: List<PortfolioEntity>
    ): HttpResult<Unit> {
        return try {
            portfolioDao.insertAll(list)
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }

    suspend fun clearPortfolios(): HttpResult<Unit> {
        return try {
            portfolioDao.deleteAll()
            HttpResult.Success(Unit)
        } catch (e: Exception) {
            HttpResult.Error(e, e.message)
        }
    }
}