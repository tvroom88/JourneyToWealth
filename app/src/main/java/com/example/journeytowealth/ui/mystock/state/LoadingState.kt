package com.example.journeytowealth.ui.mystock.state

data class LoadingState(
    val isLoading: Boolean = false,
    val message: String = "로딩중"
)