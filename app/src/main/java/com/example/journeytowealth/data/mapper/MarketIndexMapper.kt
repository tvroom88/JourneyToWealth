package com.example.journeytowealth.data.mapper

import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import com.example.journeytowealth.data.local.entity.StockEntity
import com.example.journeytowealth.data.remote.dto.MarketIndexDto
import com.example.journeytowealth.data.remote.dto.StockDto

fun MarketIndexDto.toEntity(): MarketIndexEntity =
    MarketIndexEntity(
        category = category,
        ticker = ticker,
        name = name,
        currentValue = currentValue,
        allTimeHigh = allTimeHigh,
        drawdownPercent = drawdownPercent
    )

fun MarketIndexEntity.toDto(): MarketIndexDto =
    MarketIndexDto(
        category = category,
        ticker = ticker,
        name = name,
        currentValue = currentValue,
        allTimeHigh = allTimeHigh,
        drawdownPercent = drawdownPercent
    )


fun StockDto.toEntity(): StockEntity =
    StockEntity(
        ticker = ticker,
        category = category,
        name = name,
        sector = sector,
        currentValue = currentValue.toDouble(),
        allTimeHigh = allTimeHigh.toDouble(),
        per = per?.toDouble(),
        eps = eps?.toDouble()
    )