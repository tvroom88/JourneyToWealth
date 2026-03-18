package com.example.journeytowealth.data.model

import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import com.example.journeytowealth.data.local.entity.StockEntity
import com.example.journeytowealth.ui.mystock.state.PortfolioUi


data class DbData(
    val indexes: List<MarketIndexEntity>,
    val stocks: List<StockEntity>,
    val portfolios: List<PortfolioUi>
)