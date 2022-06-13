package gg.regression

import fr.speekha.httpmocker.Mode
import fr.speekha.httpmocker.model.ResponseDescriptor
import fr.speekha.httpmocker.okhttp.builder.mockInterceptor
import okhttp3.OkHttpClient

fun getMockedClient(code: Int, body: String): OkHttpClient {
    val interceptor = mockInterceptor {
        useDynamicMocks {
            ResponseDescriptor(code = code, body = body)
        }
        setMode(Mode.ENABLED)
    }
    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
}
