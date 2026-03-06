package com.example.journeytowealth.data.model

import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import com.example.journeytowealth.data.local.entity.PortfolioEntity
import com.example.journeytowealth.data.local.entity.StockEntity

data class DbData(
    val indexes: List<MarketIndexEntity>,
    val stocks: List<StockEntity>,
    val portfolios: List<PortfolioEntity>
)