package com.yasu_k.currencyconverter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel: ViewModel() {
    var currencyFrom = ""
    var currencyTo = ""
    val rateDate: MutableLiveData<String> = MutableLiveData()
    val currencySymbol: MutableLiveData<String> = MutableLiveData()
    val exchangeRates: MutableLiveData<RateResponse> = MutableLiveData()
    val exchangeRate: MutableLiveData<Double> = MutableLiveData()
    val amountBeforeConversion: MutableLiveData<String> = MutableLiveData()

    init {
        amountBeforeConversion.value = ""
        exchangeRate.value = 0.0
        rateDate.value = ""
        currencySymbol.value = ""
    }

    fun buttonClear(){
        currencyFrom = ""
        currencyTo = ""
    }
}