package com.example.journeytowealth.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.journeytowealth.data.local.dao.MarketIndexDao
import com.example.journeytowealth.data.local.dao.PortfolioDao
import com.example.journeytowealth.data.local.dao.StockDao
import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import com.example.journeytowealth.data.local.entity.PortfolioEntity
import com.example.journeytowealth.data.local.entity.StockEntity

@Database(
    entities = [
        StockEntity::class,
        MarketIndexEntity::class,
        PortfolioEntity::class   // 추가
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stockDao(): StockDao
    abstract fun marketIndexDao(): MarketIndexDao
    abstract fun portfolioDao(): PortfolioDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}