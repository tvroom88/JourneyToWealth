package com.example.journeytowealth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock")
data class StockEntity(

    @PrimaryKey
    val ticker: String,

    val category: String,
    val name: String,
    val sector: String,

    val currentValue: Double,
    val allTimeHigh: Double,
    val drawdownPercent: Double,

    val per: Double?,
    val eps: Double?
)