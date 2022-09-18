package com.example.newsapp.data.remote

import com.example.newsapp.utils.Constants.API_KEY
import com.example.newsapp.utils.log
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newHttpUrl = request.url()
            .newBuilder()
            .addQueryParameter("apiKey", API_KEY).build()

        val newRequest = request.newBuilder()
            .url(newHttpUrl)
            .build()

        log("OUTGOING REQ ==> $newRequest")
        val response = chain.proceed(newRequest)
        log("INCOMING RES ==> $response")
        return response

    }
}