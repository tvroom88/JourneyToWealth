package com.example.journeytowealth.ui.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journeytowealth.data.model.DbData
import com.example.journeytowealth.data.repository.MainRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

//class StockViewModel(
//    private val repository: MainRepository
//) : ViewModel() {
//
//    val dbData = repository.observeDb()
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            DbData(emptyList(), emptyList())
//        )
//}