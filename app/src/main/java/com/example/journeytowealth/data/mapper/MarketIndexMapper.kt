package com.example.journeytowealth.data.mapper

import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import com.example.journeytowealth.data.local.entity.PortfolioEntity
import com.example.journeytowealth.data.local.entity.StockEntity
import com.example.journeytowealth.data.remote.dto.MarketIndexDto
import com.example.journeytowealth.data.remote.dto.PortfolioDto
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
        drawdownPercent = drawdownPercent.toDouble(),
        per = per?.toDouble(),
        eps = eps?.toDouble()
    )

fun PortfolioDto.toEntity(): PortfolioEntity =
    PortfolioEntity(
        category = category,
        etfName = etfName,
        targetWeight = targetWeight,
        currentWeight = currentWeight,
        deviation = deviation,
        tradeQuantity = tradeQuantity,
        holdingQuantity = holdingQuantity,
        currentPrice = currentPrice,
        evaluationAmount = evaluationAmount
    )

fun PortfolioEntity.toDto(): PortfolioDto =
    PortfolioDto(
        category = category,
        etfName = etfName,
        targetWeight = targetWeight,
        currentWeight = currentWeight,
        deviation = deviation,
        tradeQuantity = tradeQuantity,
        holdingQuantity = holdingQuantity,
        currentPrice = currentPrice,
        evaluationAmount = evaluationAmount
    )