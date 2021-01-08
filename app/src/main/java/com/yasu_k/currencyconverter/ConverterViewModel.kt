package com.yasu_k.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel: ViewModel() {
    var currencyFrom = ""
    var currencyTo = ""
    var exchangeRates: MutableLiveData<RateResponse> = MutableLiveData()
    var exchangeRate: Double = 0.0
    val amountBeforeConversion by lazy {
        MutableLiveData<String>()
    }

    fun buttonClear(){
        currencyFrom = ""
        currencyTo = ""
    }
}