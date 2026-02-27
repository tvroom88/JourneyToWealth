package com.example.journeytowealth.data.local

import com.example.journeytowealth.data.local.index.MarketIndexDto
import com.example.journeytowealth.data.local.portfolio.PortfolioDto
import com.example.journeytowealth.data.local.stock.StockDto

data class ExcelData(
    val indexes: List<MarketIndexDto>,
    val stocks: List<StockDto>,
    val portfolios: List<PortfolioDto>
)