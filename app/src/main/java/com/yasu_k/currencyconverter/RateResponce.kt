package com.yasu_k.currencyconverter

data class RateResponse (
        //val results: List<Result>
        val rates: Rates? = null
)

class Rates{
    var CAD: Double = 0.toDouble()
    /*var JPY: Double = 0.0
    var USD: Double = 0.0
    var EUR: Double = 0.0*/
}