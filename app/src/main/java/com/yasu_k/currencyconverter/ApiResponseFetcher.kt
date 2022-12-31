package com.yasu_k.currencyconverter

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ApiResponseFetcher(private val apiServiceXml: ApiServiceXml) {

    suspend fun getApiResponse():RateXmlResponse? {
        var result: RateXmlResponse? = null

        coroutineScope {
            launch {
                try {
                    result = apiServiceXml.getExchangeRate()
                    //Log.d("onResponse", "Succeed to fetch exchange rates")
                    System.out.println("Succeed to fetch exchange rates")
                }
                catch (e: Exception){
                    System.out.println("Exception = $e")
                    result = null
                }
            }
        }

        return result
    }
}