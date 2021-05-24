package com.yasu_k.currencyconverter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.google.android.gms.ads.*
import com.yasu_k.currencyconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object
    {
        //const val URL = "https://www.ecb.europa.eu/"
        private lateinit var mViewModel: ConverterViewModel
        private var result: RateXmlResponse? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProviders.of(this).get(ConverterViewModel::class.java)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this

        lifecycleScope.launch()
        {
            result = getApiResponse()
            setSpinners()
        }

        setAdView()
    }
    
    private fun setAdView()
    {
        val mAdView: AdView = findViewById(R.id.adView)
        MobileAds.initialize(this){}
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("AdMob", "Ad loaded")
            }

            override fun onAdFailedToLoad(errorCode: LoadAdError?) {
                Log.d("AdMob", "Failed to load the ad: $errorCode")
            }
        }
    }

    private fun getCurrencyList(): List<String>
    {
        val currencyList = mutableListOf<String>()
        val currencies = result?.currencyRates

        if (currencies != null) {
            for (aCurrency in currencies) {
                currencyList.add(aCurrency.currency)
            }
        }

        return currencyList
    }

    private fun setSpinners()
    {
        val spinnerCurrencyFrom: Spinner = findViewById(R.id.spinnerCurrencyFrom)
        val spinnerCurrencyTo: Spinner = findViewById(R.id.spinnerCurrencyTo)

        val currencyList = getCurrencyList()

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)

        spinnerCurrencyFrom.adapter = arrayAdapter
        spinnerCurrencyTo.adapter = arrayAdapter

        spinnerCurrencyFrom.onItemSelectedListener = this
        spinnerCurrencyTo.onItemSelectedListener = this
    }

    private fun calculateRate
    (
        currencyFrom:String,
        currencyTo:String,
        apiResponse:RateXmlResponse
    ):Double
    {
        val result:Double

        when {
            currencyFrom == currencyTo ->
            {
                result = 1.0
            }

            currencyTo == "EUR" ->
            {
                result = 1 / apiResponse.getRate(currencyFrom)
            }

            currencyFrom == "EUR" ->
            {
                result = apiResponse.getRate(currencyTo)
            }

            else ->
            {
                val from = apiResponse.getRate(currencyFrom)
                val to = apiResponse.getRate(currencyTo)

                result =
                    if(from != 0.0)
                    {
                        to / from
                    }
                    else
                    {
                        0.0
                    }
            }
        }

        return result
    }

    private suspend fun getApiResponse():RateXmlResponse?
    {
        try
        {
            val result = RetrofitClient.retrofitService.getExchangeRate()
            Log.d("onResponse", "Succeed to fetch exchange rates")

            /*call.enqueue(object : Callback<RateXmlResponse>
            {
                override fun onResponse(call: Call<RateXmlResponse>?, response: Response<RateXmlResponse>)
                {
                    if (response.isSuccessful)
                    {
                        Log.d("onResponse", "Succeed to fetch exchange rates\nresult: ${response.body()}")
                        result = response.body()
                    }
                }

                override fun onFailure(call: Call<RateXmlResponse>?, t: Throwable?) {
                    result = null
                    Log.e("onFailure", t.toString())
                }
            })*/

            return result
        }
        catch (e: Exception)
        {
            return null
        }
    }

    private fun getCurrentRate()
    {
        if (result != null)
        {
            val currencyFrom = mViewModel.currencyFrom.value
            val currencyTo = mViewModel.currencyTo.value

            mViewModel.exchangeRate.value = calculateRate(currencyFrom!!, currencyTo!!, result!!)

            //LiveData
            mViewModel.exchangeRates.postValue(result)//--> How to check the value in livedata??

            Log.d("tvRate", "I'm gonna update the exchange rate!!")
        }
        else
        {
            mViewModel.exchangeRates.postValue(null)
            Log.e("getCurrentRate","API call fail")
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long)
    {
        when (parent.id){
            R.id.spinnerCurrencyFrom -> {
                mViewModel.currencyFrom.value = "" + parent.getItemAtPosition(position)
                getCurrentRate()
            }

            R.id.spinnerCurrencyTo -> {
                mViewModel.currencyTo.value = "" + parent.getItemAtPosition(position)
                getCurrentRate()
            }

            else -> Log.d("onItemSelected", "An error occurred")
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}