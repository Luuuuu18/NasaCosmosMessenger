package com.example.nasacosmosmessenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasacosmosmessenger.data.remote.ApodApi
import com.example.nasacosmosmessenger.model.ChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class NovaViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val apiKey = "OgKfRTK2o8ibax2sPzumxIC0eeif5eP5sCIkx7KY"

    private var cachedTodayApod: com.example.nasacosmosmessenger.data.remote.ApodResponse? = null

    private suspend fun <T> safeApiCall(maxRetries: Int = 3, block: suspend () -> T): T {
        var currentAttempt = 0
        while (true) {
            try {
                return block() // 嘗試執行 API 呼叫
            } catch (e: Exception) {
                currentAttempt++
                // 如果是 500, 502, 503, 504 伺服器端錯誤，或者是網路連線異常 (IOException)
                val isTransientError = (e is retrofit2.HttpException && e.code() in 500..504) || e is java.io.IOException

                // 如果超過重試次數，或者這不是一個可以重試的錯誤（例如 403 密碼錯、400 格式錯），就直接放棄
                if (currentAttempt >= maxRetries || !isTransientError) {
                    throw e
                }

                // 偷偷等待一下再試 (第一次等1秒，第二次等2秒...)
                delay(1000L * currentAttempt)
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            content = text,
            imageUrl = null,
            date = null,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )

        _messages.value = _messages.value + userMsg

        viewModelScope.launch {
            try {
                val date = extractDate(text)

                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                sdf.timeZone = java.util.TimeZone.getTimeZone("America/New_York")
                val nasaToday = sdf.format(java.util.Date())

                // 情況一：輸入非日期
                if (date == null) {
                    val welcomeMsg = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        content = "歡迎！輸入日期我會告訴你那天宇宙長什麼樣子。",
                        imageUrl = null,
                        date = null,
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )

                    // 🔥 關鍵修改：將 API 呼叫包裝進 safeApiCall 裡面
                    val response = cachedTodayApod ?: safeApiCall {
                        ApodApi.service.getApodByDate(nasaToday, apiKey)
                    }.also {
                        cachedTodayApod = it
                    }

                    val textMsg = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        content = "這是今天的 APOD：",
                        imageUrl = null,
                        date = null,
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )

                    val imageMsg = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        content = buildContent(response),
                        imageUrl = if (response.media_type == "image") response.url else null,
                        date = response.date,
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )

                    _messages.value = _messages.value + listOf(welcomeMsg, textMsg, imageMsg)
                    return@launch
                }

                // 情況二：輸入未來日期
                if (date > nasaToday) {
                    val errorMsg = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        content = "不能查詢未來日期",
                        imageUrl = null,
                        date = null,
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )

                    _messages.value = _messages.value + errorMsg
                    return@launch
                }

                // 情況三：輸入過去或今天日期
                // 🔥 關鍵修改：同樣將指定日期的查詢也包裝起來防呆
                val response = safeApiCall {
                    ApodApi.service.getApodByDate(date, apiKey)
                }

                val textMsg = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    content = "那天宇宙長這樣...",
                    imageUrl = null,
                    date = null,
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )

                val imageMsg = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    content = buildContent(response),
                    imageUrl = if (response.media_type == "image") response.url else null,
                    date = response.date,
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )

                _messages.value = _messages.value + listOf(textMsg, imageMsg)

            } catch (e: Exception) {
                val errorText = when (e) {
                    is retrofit2.HttpException -> {
                        when (e.code()) {
                            400 -> "請求錯誤（日期格式錯誤）"
                            403 -> "API Key 無效或權限不足"
                            404 -> "查無資料（該日期沒有 APOD）"
                            429 -> "請求過多（API 次數用完）"
                            // 503 雖然會被 safeApiCall 攔截重試，但如果試了 3 次還是 503，就會走到這裡
                            500, 502, 503, 504 -> "NASA 伺服器目前太忙碌，請稍後再試（${e.code()}）"
                            else -> "伺服器錯誤：${e.code()}"
                        }
                    }

                    is java.net.UnknownHostException -> "無法連線，請檢查網路"
                    is java.net.SocketTimeoutException -> "連線逾時，請稍後再試"
                    else -> "發生錯誤：${e.message}"
                }

                val errorMsg = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    content = errorText,
                    imageUrl = null,
                    date = null,
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )

                _messages.value = _messages.value + errorMsg
            }
        }
    }

    private fun extractDate(text: String): String? {
        val regex1 = Regex("""\d{4}/\d{2}/\d{2}""")
        val regex2 = Regex("""\d{4}-\d{2}-\d{2}""")

        return when {
            regex1.containsMatchIn(text) ->
                regex1.find(text)?.value?.replace("/", "-")

            regex2.containsMatchIn(text) ->
                regex2.find(text)?.value

            else -> null
        }
    }

    private fun buildContent(response: com.example.nasacosmosmessenger.data.remote.ApodResponse): String {
        return if (response.media_type == "video") {
            """
            ${response.title}
            
            ${response.date}
            
            ${response.url}
            
            ${response.explanation}
            """.trimIndent()
        } else {
            """
            ${response.title}
            
            ${response.date}
            
            ${response.explanation}
            """.trimIndent()
        }
    }

    fun toggleFavorite(message: ChatMessage) {}
}