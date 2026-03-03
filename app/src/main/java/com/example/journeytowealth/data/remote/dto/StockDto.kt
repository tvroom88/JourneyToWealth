package com.example.journeytowealth.data.remote.dto

data class StockDto(
    val category: String,
    val name: String,          // 종목 이름
    val ticker: String,        // 종목 코드
    val sector: String,
    val currentValue: Float,    // 현재 값
    val allTimeHigh: Float,     // 전고점
    val percentBtwMaxAndCur: Float, // 등락률
    val per: Float?,
    val eps: Float?
)