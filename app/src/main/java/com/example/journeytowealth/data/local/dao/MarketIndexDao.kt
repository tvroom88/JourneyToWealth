package com.example.journeytowealth.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketIndexDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(list: List<MarketIndexEntity>)

    @Query("SELECT * FROM market_index")
    fun getAll(): Flow<List<MarketIndexEntity>>

    @Query("DELETE FROM market_index")
    suspend fun deleteAll()
}