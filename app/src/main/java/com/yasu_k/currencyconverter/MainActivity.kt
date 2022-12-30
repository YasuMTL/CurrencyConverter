package com.yasu_k.currencyconverter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.*
import com.yasu_k.currencyconverter.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object
    {
        private lateinit var mViewModel: ConverterViewModel
        private var result: RateXmlResponse? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProvider(this).get(ConverterViewModel::class.java)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this

        lifecycleScope.launch()
        {
            //result = getApiResponse()
            result = ApiResponseFetcher(RetrofitClient.retrofitService).getApiResponse()
            println("result = $result")
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

            override fun onAdFailedToLoad(errorCode: LoadAdError) {
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

        currencyList.add("EUR")
        currencyList.sortWith(String.CASE_INSENSITIVE_ORDER)

        //test
//        for(aCurrency:String in currencyList)
//        {
//            Log.i("currency", aCurrency)
//        }

        return currencyList
    }

    private fun setSpinners()
    {
        val spinnerCurrencyFrom: Spinner = findViewById(R.id.spinnerCurrencyFrom)
        val spinnerCurrencyTo: Spinner = findViewById(R.id.spinnerCurrencyTo)

        val currencyList = getCurrencyList()
        val flagList: Array<Int> = arrayOf(
                    R.drawable.au,
                    R.drawable.bg,
                    R.drawable.br,
                    R.drawable.ca,
                    R.drawable.ch,
                    R.drawable.cn,
                    R.drawable.cz,
                    R.drawable.dk,
                    R.drawable.eu,
                    R.drawable.gb,
                    R.drawable.hk,
                    R.drawable.hr,
                    R.drawable.hu,
                    R.drawable.id,
                    R.drawable.il,
                    R.drawable.ind,
                    R.drawable.isl,
                    R.drawable.jp,
                    R.drawable.kr,
                    R.drawable.mx,
                    R.drawable.my,
                    R.drawable.no,
                    R.drawable.nz,
                    R.drawable.ph,
                    R.drawable.pl,
                    R.drawable.ro,
                    R.drawable.ru,
                    R.drawable.se,
                    R.drawable.sg,
                    R.drawable.th,
                    R.drawable.tr,
                    R.drawable.us,
                    R.drawable.za)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        // test
        //val flagAdapter = FlagAdapter(applicationContext, )
        val flagAdapter = FlagAdapter(applicationContext, flagList, currencyList)

        //test
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)

        //spinnerCurrencyFrom.adapter = arrayAdapter
        spinnerCurrencyFrom.adapter = flagAdapter
        spinnerCurrencyTo.adapter = flagAdapter

        spinnerCurrencyFrom.onItemSelectedListener = this
        spinnerCurrencyTo.onItemSelectedListener = this
    }

    fun calculateRate
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
                //mViewModel.currencyTo.value = "" + R.id.spinnerCurrencyTo.getItemAtPosition(position)
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