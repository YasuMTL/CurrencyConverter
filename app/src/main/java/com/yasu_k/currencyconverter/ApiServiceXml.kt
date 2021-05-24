package com.yasu_k.currencyconverter

import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceXml {
    @GET("stats/eurofxref/eurofxref-daily.xml")
    //fun getExchangeRate() : Call<RateXmlResponse> // Without Coroutine
    suspend fun getExchangeRate() : RateXmlResponse
}