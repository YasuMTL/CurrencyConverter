package com.yasu_k.currencyconverter

import retrofit2.http.GET

interface ApiServiceXml {
    @GET("stats/eurofxref/eurofxref-daily.xml")
    suspend fun getExchangeRate() : RateXmlResponse
}