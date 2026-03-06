package com.example.journeytowealth.data.remote.dto

data class PortfolioDto(
    val idForDb: Int,
    val category: String,          // 표시 (ETF 이름)
    val etfName: String,        // ETF 코드
    val targetWeight: Float,   // 목표 비중 (%)
    val currentWeight: Float,  // 실제 비중 (%)
    val deviation: Float,      // 이격율 (%)
    val tradeQuantity: Int,    // 매매 수량
    val holdingQuantity: Int,  // 보유 수량
    val currentPrice: Long,    // 현재가
    val evaluationAmount: Long // 평가액
)