package com.yasu_k.currencyconverter

import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceXml {
    @GET("stats/eurofxref/eurofxref-daily.xml")
    //@ResponseFormat("xml")
//    fun getExchangeRate(@Query("base") currencyFrom: String?,
//                        @Query("symbols") currencyTo: String?) : Call<RateXmlResponse>
    fun getExchangeRate() : Call<RateXmlResponse>
}