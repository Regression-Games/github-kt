package gg.regression.github.exceptions

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class HttpClientException(val code: Int, val body: String) : Exception() {

    inline fun <reified T> toObject(): T {
        val jsonMapper = jacksonObjectMapper()
        jsonMapper.registerModule(JavaTimeModule())
        return jsonMapper.readValue(body, T::class.java)
    }
}
