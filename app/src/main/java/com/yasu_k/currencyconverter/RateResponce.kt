package com.yasu_k.currencyconverter

import com.google.gson.annotations.SerializedName

data class RateResponse (
        @SerializedName("rates") var rates: Currencies,
        @SerializedName("base") var base: String? = null,
        @SerializedName("date") var date: String? = null
)

data class Currencies(
        var CAD: Double,
        var USD: Double,
        var EUR: Double,
        var JPY: Double
)