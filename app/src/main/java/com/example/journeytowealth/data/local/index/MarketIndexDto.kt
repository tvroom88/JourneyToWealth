package com.example.journeytowealth.data.local.index

data class MarketIndexDto(
    val name: String,        // 지수 이름, 예: KOSPI
    val ticker: String,      // 지수 코드, 예: ^KS11
    val currentValue: Float, // 현재 값
    val maxValue: Float,     // 등락
    val percentBtwMaxAndCur: Float // 등락률
)
