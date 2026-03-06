package com.example.journeytowealth.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.core.utils.ExcelParser
import com.example.journeytowealth.data.repository.MainRepository
import com.example.journeytowealth.ui.state.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _loading = MutableStateFlow(LoadingState())
    val loading = _loading.asStateFlow()

    // Excel 다운로드 실행
    fun loadExcel(accessToken: String?) {
        accessToken ?: return
        viewModelScope.launch {
            showLoading("엑셀 다운로드 중입니다.")

            when (val result = repository.downloadExcel(accessToken)) {
                is HttpResult.Success -> {
                    showLoading("엑셀 데이터를 파싱중입니다..")
                    val list = ExcelParser.parse(result.data)

                    for(myData in list.portfolios ){
                        Log.d("myData", myData.category)
                    }

                    showLoading("DB에 데이터를 저장하는 중입니다.")
                    repository.insertExcelToDb(list)

                    hideLoading()
                }

                is HttpResult.Error -> {
                    hideLoading()
                }

                else -> {}
            }
        }

    }

    fun showLoading(message: String? = null) {
        _loading.value = LoadingState(true, message ?: "로딩중")
    }


    fun hideLoading() {
        _loading.value = LoadingState(false, "")
    }
}