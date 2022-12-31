package com.yasu_k.currencyconverter

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://www.ecb.europa.eu/"
    private val converter = XmlConverterFactory.create()
    private val okHttpClient= OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    private fun retrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converter)
            .client(okHttpClient)
            .build()
    }

    val retrofitService: ApiServiceXml by lazy {
        retrofit().create(ApiServiceXml::class.java)
    }
}