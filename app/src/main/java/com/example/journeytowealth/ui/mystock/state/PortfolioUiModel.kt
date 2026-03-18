package com.example.journeytowealth.ui.mystock.state

sealed interface PortfolioUi {
    data class AccountType(val name: String) : PortfolioUi
    data class Header(val data: PortfolioHeaderUiModel) : PortfolioUi
    data class Item(val data: PortfolioItemUiModel) : PortfolioUi
}

data class PortfolioHeaderUiModel(
    val etfName: String,
    val targetWeight: String,
    val currentWeight: String,
    val deviation: String,
    val tradeQuantity: String,
    val holdingQuantity: String,
    val currentPrice: String,
    val evaluationAmount: String
)

data class PortfolioItemUiModel(
    val etfName: String,
    val targetWeight: Float,
    val currentWeight: Float,
    val deviation: Float,
    val tradeQuantity: Int,
    val holdingQuantity: Int,
    val currentPrice: Long,
    val evaluationAmount: Long
)
