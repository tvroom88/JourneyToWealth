package com.example.journeytowealth.data.model

import com.example.journeytowealth.data.remote.dto.MarketIndexDto
import com.example.journeytowealth.data.remote.dto.PortfolioDto
import com.example.journeytowealth.data.remote.dto.StockDto

data class ExcelData(
    val indexes: List<MarketIndexDto>,
    val stocks: List<StockDto>,
    val portfolios: List<PortfolioDto>
)