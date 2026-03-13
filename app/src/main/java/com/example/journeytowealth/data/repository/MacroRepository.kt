package com.example.journeytowealth.data.repository

import android.content.Context
import com.example.journeytowealth.core.constant.Constants
import com.example.journeytowealth.core.utils.FileDownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MacroRepository(private val context: Context) {
    // 메모리에 캐시해둘 리스트
    private var cachedGdp: List<Pair<String, String>>? = null

    fun downloadMacroCsvFile() {
        FileDownloadManager.downloadFile(
            context = context,
            url = Constants.Url.US_GDP_CSV_URL,
            fileName = Constants.FileName.US_GDP_FILE,
            title = "미국 GDP csv 파일 다운로드중...",
            description = "파일 가져오는 중 입니다."
        )
    }

    suspend fun getGdpData(refresh: Boolean = false, year: Int = 2022): List<Pair<String, String>> {
        return withContext(Dispatchers.IO) {
            // 1. 새로고침이 아니고 캐시가 있으면 바로 반환
            if (!refresh && cachedGdp != null) return@withContext cachedGdp!!

            // 2. 파일에서 읽어오기
            val rawData = FileDownloadManager.readCsvFile(context, Constants.FileName.US_GDP_FILE) { date, _ ->
                (date.take(4).toIntOrNull() ?: 0) >= year
            }

            // 3. 캐시 업데이트 후 반환
            cachedGdp = rawData
            rawData
        }
    }
}