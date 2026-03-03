package com.example.journeytowealth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market_index")
data class MarketIndexEntity(
    @PrimaryKey
    val ticker: String,          // 지수 코드를 PK로 사용 (^KS11 등)
    val category: String,
    val name: String,
    val currentValue: Float,
    val allTimeHigh: Float,
    val drawdownPercent: Float
)

