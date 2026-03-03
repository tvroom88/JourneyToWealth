package com.example.journeytowealth.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    // UI에서 observe할 StateFlow
    private val _excelData = MutableStateFlow<HttpResult<Unit>>(HttpResult.Loading)
    val excelData: StateFlow<HttpResult<Unit>> = _excelData

    // Excel 다운로드 실행
    fun loadExcel(accessToken: String?) {
        accessToken?.let {
            viewModelScope.launch {
                _excelData.value = HttpResult.Loading
                val result = repository.refreshExcelData(accessToken)
                _excelData.value = result
            }
        }
    }
}