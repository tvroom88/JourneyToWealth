package com.example.journeytowealth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class PortfolioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val etfName: String,
    val targetWeight: Float,
    val currentWeight: Float,
    val deviation: Float,
    val tradeQuantity: Int,
    val holdingQuantity: Int,
    val currentPrice: Long,
    val evaluationAmount: Long
)