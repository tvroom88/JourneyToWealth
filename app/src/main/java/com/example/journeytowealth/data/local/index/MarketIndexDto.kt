package com.example.journeytowealth.data.local.index

data class MarketIndexDto(
    val name: String,           // 지수 이름 (ex. S&P 500)
    val ticker: String,         // 지수 코드, 예: ^KS11
    val currentValue: Float,    // 현재값
    val allTimeHigh: Float,     // 전고점
    val drawdownPercent: Float  // 전고점 대비 하락율
)
