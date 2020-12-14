package com.yasu_k.currencyconverter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel: ViewModel() {
    var currencyFrom = ""
    var currencyTo = ""
    var exchangeRates: MutableLiveData<RateResponse> = MutableLiveData()
}