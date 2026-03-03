package com.example.journeytowealth.data.remote

import android.util.Log
import com.example.journeytowealth.core.result.HttpResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ExcelRemoteDataSource {

    private val client = OkHttpClient()

    suspend fun downloadGoogleSheet(accessToken: String): HttpResult<ByteArray> =
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(ExcelApiConstants.DOWNLOAD_PATH)
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()

                client.newCall(request).execute().use { response ->
                    val bodyBytes = response.body?.bytes()
                        ?: return@withContext HttpResult.Error(
                            Exception("Empty body"),
                            "Downloaded Excel is empty"
                        )

                    if (!response.isSuccessful) {
                        return@withContext HttpResult.Error(
                            Exception("HTTP ${response.code}"),
                            "Failed to download Excel"
                        )
                    }

                    HttpResult.Success(bodyBytes)
                }
            } catch (e: Exception) {
                Log.d("ExcelRemoteDataSource", Log.getStackTraceString(e))
                HttpResult.Error(e, e.message)
            }
        }
}
