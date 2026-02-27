package com.example.journeytowealth.data.local.stock

data class StockDto(
    val ticker: String,        // 종목 코드
    val name: String,          // 종목 이름
    val currentValue: Float, // 현재 값
    val maxValue: Float,     // 등락
    val percentBtwMaxAndCur: Float // 등락률
)
