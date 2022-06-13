package gg.regression.github

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import gg.regression.github.exceptions.HttpClientException
import gg.regression.github.exceptions.UnauthorizedException
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

open class HttpClient {

    var httpClient: OkHttpClient = OkHttpClient()
    val jsonMapper = jacksonObjectMapper()
    val JSON: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull()!! // .toMediaTypeOrNull()!!

    init {
        jsonMapper.registerModule(JavaTimeModule())
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @Throws(HttpClientException::class, UnauthorizedException::class)
    inline fun <reified T> get(url: String, headers: Map<String, String> = mapOf()): T {
        val request: Request = Request.Builder()
            .url(url)
            .apply { headers.entries.forEach { this.header(it.key, it.value) } }
            .build()
        httpClient.newCall(request).execute().use { response ->
            val jsonResult = response.body!!.string()
            try {
                return jsonMapper.readValue(jsonResult)
            } catch (exception: Exception) {
                if (response.code == 401) {
                    throw UnauthorizedException()
                }
                println(exception)
                throw HttpClientException(response.code, jsonResult)
            }
        }
    }

    @Throws(HttpClientException::class, UnauthorizedException::class)
    inline fun <reified T> post(url: String, data: Any, headers: Map<String, String> = mapOf()): T {
        val dataAsJson = jsonMapper.writeValueAsString(data)
        val body = dataAsJson.toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url)
            .apply { headers.entries.forEach { this.header(it.key, it.value) } }
            .post(body)
            .build()
        httpClient.newCall(request).execute().use { response ->
            val jsonResult = response.body!!.string()
            try {
                return jsonMapper.readValue(jsonResult)
            } catch (exception: Exception) {
                if (response.code == 401) {
                    throw UnauthorizedException()
                }
                throw HttpClientException(response.code, jsonResult)
            }
        }
    }
}
