package com.example.journeytowealth.core.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import java.io.File

object FredDownloadManager {

    /**
     * 파일 다운로드 요청
     * @param context: 컨텍스트
     * @param url: 다운로드할 전체 URL
     * @param fileName: 저장할 파일명 (확장자 포함 권장, 예: "data.csv")
     * @param title: 알림창에 표시될 제목
     */
    fun downloadFile(
        context: Context,
        url: String,
        fileName: String,
        title: String = "파일 다운로드",
        description: String = "파일을 가져오는 중입니다..."
    ): Long {
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        return try {
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                setTitle(title)
                setDescription(description)

                // 알림 표시 설정
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                // 앱 전용 문서 디렉토리에 저장
                setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOCUMENTS, fileName)

                // 파일 확장자에 따른 MIME 타입 추측 (기본값: binary)
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl(url)
                ) ?: "application/octet-stream"
                setMimeType(mimeType)
            }

            manager.enqueue(request) // 다운로드 ID 반환
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }

    /**
     * 저장된 CSV 파일 읽기 (범용 필터링)
     * @param context: 컨텍스트
     * @param fileName: 읽을 파일 이름
     * @param filterPredicate: 각 줄(Line)에 대해 포함 여부를 결정하는 조건식 (람다)
     */
    fun readCsvFile(
        context: Context,
        fileName: String,
        filterPredicate: (date: String, value: String) -> Boolean = { _, _ -> true }
    ): List<Pair<String, String>> {
        val resultList = mutableListOf<Pair<String, String>>()
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        if (!file.exists()) return emptyList()

        try {
            file.bufferedReader().useLines { lines ->
                lines.forEachIndexed { index, line ->
                    if (index == 0) return@forEachIndexed // 헤더 건너뜀

                    val tokens = line.split(",")
                    if (tokens.size >= 2) {
                        val date = tokens[0].trim()
                        val value = tokens[1].trim()

                        // 외부에서 전달한 조건(filterPredicate)이 맞을 때만 리스트에 추가
                        if (filterPredicate(date, value)) {
                            resultList.add(Pair(date, value))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return resultList
    }
}