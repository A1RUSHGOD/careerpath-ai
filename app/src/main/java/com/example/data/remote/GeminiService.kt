package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String? =
        withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                Log.w("GeminiService", "GEMINI_API_KEY is not configured or is placeholder. Using smart algorithmic engine.")
                return@withContext null
            }

            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

                val rootJson = JSONObject()
                val contentsArray = JSONArray()
                val contentObj = JSONObject()
                val partsArray = JSONArray()

                val partObj = JSONObject()
                partObj.put("text", prompt)
                partsArray.put(partObj)

                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
                rootJson.put("contents", contentsArray)

                if (!systemInstruction.isNullOrBlank()) {
                    val systemObj = JSONObject()
                    val systemParts = JSONArray()
                    val sysPart = JSONObject()
                    sysPart.put("text", systemInstruction)
                    systemParts.put(sysPart)
                    systemObj.put("parts", systemParts)
                    rootJson.put("systemInstruction", systemObj)
                }

                val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseBodyString = response.body?.string()

                if (!response.isSuccessful || responseBodyString == null) {
                    Log.e("GeminiService", "API call failed with code: ${response.code}, body: $responseBodyString")
                    return@withContext null
                }

                val responseJson = JSONObject(responseBodyString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text")
                    }
                }
                null
            } catch (e: Exception) {
                Log.e("GeminiService", "Error calling Gemini API: ${e.localizedMessage}")
                null
            }
        }
}
