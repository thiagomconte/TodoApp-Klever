package com.example.data.app.remote

import com.example.data.app.util.GlobalNavigator
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    var token: String = ""
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")

        val request = requestBuilder.build()
        val response = chain.proceed(request)
        if (response.code == 401) {
            GlobalNavigator.logout()
        }
        return response
    }
}
