package com.yasu_k.currencyconverter

import android.util.Log
import kotlinx.coroutines.*

//class ApiResponseFetcher(val retrofitClient: RetrofitClient) {
class ApiResponseFetcher(private val apiServiceXml: ApiServiceXml) {

    //suspend fun getApiResponse():RateXmlResponse? = withContext(Dispatchers.IO)
    suspend fun getApiResponse():RateXmlResponse? {
        var result: RateXmlResponse? = null

        coroutineScope {
            launch {
            //async {
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

            //job.join()
        }

        return result
    }

//    suspend fun testFunc(): String {
//        delay(100L)
//        return "foo"
//    }
}