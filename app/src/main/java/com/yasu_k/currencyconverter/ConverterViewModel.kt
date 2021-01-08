package com.yasu_k.currencyconverter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel: ViewModel() {
    var currencyFrom = ""
    var currencyTo = ""
    val exchangeRates: MutableLiveData<RateResponse> = MutableLiveData()
    //var exchangeRate: Double = 0.0
    var exchangeRate: MutableLiveData<Double> = MutableLiveData()
    val amountBeforeConversion: MutableLiveData<String> = MutableLiveData()
    init {
        amountBeforeConversion.value = ""
        exchangeRate.value = 0.0
    }
    //val a: LiveData<String> = amountBeforeConversion

    fun buttonClear(){
        currencyFrom = ""
        currencyTo = ""
    }
}