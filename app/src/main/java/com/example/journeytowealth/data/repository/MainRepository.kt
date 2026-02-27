package com.example.journeytowealth.data.repository

import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.core.utils.ExcelParser
import com.example.journeytowealth.data.local.ExcelData
import com.example.journeytowealth.data.remote.ExcelRemoteDataSource

class MainRepository(private val excelRemoteDataSource: ExcelRemoteDataSource) {

    /**
     * Excel 다운로드 후 DTO 변환
     */
    suspend fun fetchExcelData(accessToken: String): HttpResult<ExcelData> {
        return when (val result = excelRemoteDataSource.downloadGoogleSheet(accessToken)) {
            is HttpResult.Success -> {
                try {
                    val excelData = ExcelParser.parse(result.data)
                    HttpResult.Success(excelData)
                } catch (e: Exception) {
                    HttpResult.Error(e, "Failed to parse Excel")
                }
            }
            is HttpResult.Error -> HttpResult.Error(result.exception, result.message)
            else -> HttpResult.Loading  // Loading 처리
        }
    }
}