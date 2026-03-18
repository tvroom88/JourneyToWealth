package com.example.journeytowealth.data.mapper

import android.util.Log
import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import com.example.journeytowealth.data.local.entity.PortfolioEntity
import com.example.journeytowealth.data.local.entity.StockEntity
import com.example.journeytowealth.data.remote.dto.MarketIndexDto
import com.example.journeytowealth.data.remote.dto.PortfolioDto
import com.example.journeytowealth.data.remote.dto.StockDto
import com.example.journeytowealth.ui.mystock.state.PortfolioHeaderUiModel
import com.example.journeytowealth.ui.mystock.state.PortfolioItemUiModel
import com.example.journeytowealth.ui.mystock.state.PortfolioUi

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
        idForDb = idForDb,
        accountType = category,
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
        idForDb = idForDb,
        category = accountType,
        etfName = etfName,
        targetWeight = targetWeight,
        currentWeight = currentWeight,
        deviation = deviation,
        tradeQuantity = tradeQuantity,
        holdingQuantity = holdingQuantity,
        currentPrice = currentPrice,
        evaluationAmount = evaluationAmount
    )

fun PortfolioEntity.toUiModel(): PortfolioItemUiModel =
    PortfolioItemUiModel(
        etfName = etfName,
        targetWeight = targetWeight,
        currentWeight = currentWeight,
        deviation = deviation,
        tradeQuantity = tradeQuantity,
        holdingQuantity = holdingQuantity,
        currentPrice = currentPrice,
        evaluationAmount = evaluationAmount
    )


fun List<PortfolioEntity>.toSectionUiModel(): List<PortfolioUi> {

    Log.d("CheckCheck", "1. Mapper에 들어온 원본 리스트 개수: ${this.size}")

    // 1. 데이터 그룹화 (기존 로직 유지)
    val groups = mutableMapOf<Any?, MutableList<PortfolioEntity>>()
    for (entity in this) {
        val key = entity.accountType
        if (!groups.containsKey(key)) {
            groups[key] = mutableListOf()
        }
        groups[key]?.add(entity)

        Log.d("CheckCheck", "entity: $entity)")
        // [확인 2] ISA 계좌에 데이터가 쌓일 때마다 로그 출력
//        if (key.toString().contains("ISA")) {
//            Log.d("CheckCheck", "ISA 계좌에 종목 추가 중: ${entity.etfName} (현재 해당 그룹 크기: ${groups[key]?.size})")
//        }
    }

    // 2. 결과 리스트 생성 (Flat List 방식)
    val result = mutableListOf<PortfolioUi>()

    for ((type, entities) in groups) {
        // (A) 계좌 타입 추가 (TYPE_ACCOUNT)
        val accountName = type as? String ?: ""
        Log.d("accountName", "aaacountNma : $accountName")
        result.add(PortfolioUi.AccountType(accountName))

        // (B) 공통 헤더 추가 (TYPE_HEADER)
        // PortfolioHeaderUiModel에 데이터가 있다면 생성자에 전달
        result.add(
            PortfolioUi.Header(
                PortfolioHeaderUiModel(
                    "ETF", "목표 비중", "실제 비중", "이격률",
                    "매매수량", "보유수량", "현재가", "평가액"
                )
            )
        )

        // (C) 각 종목 아이템들 추가 (TYPE_ITEM)
        for (item in entities) {
            result.add(
                PortfolioUi.Item(
                    PortfolioItemUiModel(
                        etfName = item.etfName,
                        currentWeight = item.currentWeight,
                        deviation = item.deviation,
                        tradeQuantity = item.tradeQuantity,
                        holdingQuantity = item.holdingQuantity,
                        currentPrice = item.currentPrice,
                        targetWeight = item.targetWeight,
                        evaluationAmount = item.evaluationAmount
                    )
                )
            )
        }
    }
    return result
}