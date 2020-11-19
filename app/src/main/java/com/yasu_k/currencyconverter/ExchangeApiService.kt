package com.yasu_k.currencyconverter

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApiService {
    @GET("latest")
    fun getExchangeRate(@Query("base") currencyFrom: String,
                        @Query("symbols") currencyTo: String) : Call<RateResponse>
}