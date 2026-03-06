package com.example.journeytowealth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class PortfolioEntity(
    @PrimaryKey
    val idForDb: Int,

    val etfName: String,

    val accountType: String,   // ISA / 연금저축 / DC

    val targetWeight: Float,
    val currentWeight: Float,
    val deviation: Float,
    val tradeQuantity: Int,
    val holdingQuantity: Int,
    val currentPrice: Long,
    val evaluationAmount: Long
)