package com.example.journeytowealth.ui.toprightmenu.macroeconomic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journeytowealth.data.repository.MacroRepository
import kotlinx.coroutines.launch

class MacroViewModel(private val repository: MacroRepository) : ViewModel() {

    // UI에 보여줄 상태 데이터 (성장률, 증감폭 등)
    private val _gdpYoY = MutableLiveData<String>() // 예: "+0.5%"
    val gdpYoY: LiveData<String> = _gdpYoY

    private val _gdpMom = MutableLiveData<String>() // 예: "4.1%"
    val gdpMom: LiveData<String> = _gdpMom

    fun downloadUsaGdpCsvFile() {
        viewModelScope.launch {
            repository.downloadMacroCsvFile()
        }
    }

    fun loadMacroData() {
        viewModelScope.launch {
            val data = repository.getGdpData() // List<Pair<String, String>>

            if (data.size >= 5) { // 최소 1년치(4분기) + 현재 데이터가 있어야 YoY 가능
                val current = data.last().second.toDoubleOrNull() ?: 0.0
                val previous = data[data.size - 2].second.toDoubleOrNull() ?: 0.0
                val lastYear = data[data.size - 5].second.toDoubleOrNull() ?: 0.0

                // 1. YoY
                val yoyRate = ((current - lastYear) / lastYear) * 100
                _gdpYoY.value = String.format("%s%.1f%%", if (yoyRate >= 0) "+" else "", yoyRate)

                // 2. MoM
                val momRate = ((current - previous) / previous) * 100
                _gdpMom.value = String.format("%s%.1f%%", if (momRate >= 0) "+" else "", momRate)
            }
        }
    }
}
