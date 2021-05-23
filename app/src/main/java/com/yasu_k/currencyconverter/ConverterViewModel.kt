package com.yasu_k.currencyconverter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel: ViewModel() {
    val currencyFrom: MutableLiveData<String> = MutableLiveData()
    val currencyTo: MutableLiveData<String> = MutableLiveData()
    val rateDate: MutableLiveData<String> = MutableLiveData()
    val currencySymbol: MutableLiveData<String> = MutableLiveData()
    val exchangeRates: MutableLiveData<RateXmlResponse> = MutableLiveData()
    val exchangeRate: MutableLiveData<Double> = MutableLiveData()
    val amountBeforeConversion: MutableLiveData<String> = MutableLiveData()

    init {
        currencyFrom.value = ""
        currencyTo.value = ""
        amountBeforeConversion.value = ""
        exchangeRate.value = 0.0
        rateDate.value = ""
        currencySymbol.value = ""
    }

    fun buttonClear(){
        amountBeforeConversion.value = ""
    }
}