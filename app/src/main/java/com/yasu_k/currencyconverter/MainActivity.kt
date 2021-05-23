package com.yasu_k.currencyconverter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.google.android.gms.ads.*
import com.yasu_k.currencyconverter.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object
    {
        const val URL = "https://www.ecb.europa.eu/"
        private lateinit var mViewModel: ConverterViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProviders.of(this).get(ConverterViewModel::class.java)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this

        setSpinners()
        // Turn Ad off for now
        //setAdView()
    }
    
    private fun setAdView(){
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

    private fun setSpinners(){
        val spinnerCurrencyFrom: Spinner = findViewById(R.id.spinnerCurrencyFrom)
        val spinnerCurrencyTo: Spinner = findViewById(R.id.spinnerCurrencyTo)

        spinnerCurrencyFrom.onItemSelectedListener = this
        spinnerCurrencyTo.onItemSelectedListener = this
    }

    private fun getCurrentRate(){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val converter = XmlConverterFactory.create()
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(converter)
            .client(okHttpClient)
            .build()

        //val service = retrofit.create(ExchangeApiService::class.java)
        val service = retrofit.create(ApiServiceXml::class.java)
        //val call = service.getExchangeRate(mViewModel.currencyFrom.value, mViewModel.currencyTo.value)
        val call = service.getExchangeRate()
        Log.d("CURRENCY", "currencyFrom: ${mViewModel.currencyFrom} currencyTo: ${mViewModel.currencyTo}")

        call.enqueue(object : Callback<RateXmlResponse> {
            override fun onResponse(call: Call<RateXmlResponse>?, response: Response<RateXmlResponse>) {
                val apiResponse = response.body()

                if (response.isSuccessful && apiResponse != null)
                {
                    val currencyFrom = mViewModel.currencyFrom.value
                    val currencyTo = mViewModel.currencyTo.value

                    if (currencyFrom.equals(currencyTo) )
                    {
                        mViewModel.exchangeRate.value = 1.0
                    }
                    else if (currencyTo.equals("EUR") )
                    {
                        mViewModel.exchangeRate.value = 1 / apiResponse.getRate(currencyFrom!!)
                    }
                    else if(currencyFrom.equals("EUR") )
                    {
                        mViewModel.exchangeRate.value = apiResponse.getRate(currencyTo!!)
                    }
                    else
                    {
                        val from = apiResponse.getRate(currencyFrom!!)
                        val to = apiResponse.getRate(currencyTo!!)

                        if(from != 0.0)
                        {
                            mViewModel.exchangeRate.value = to / from
                        }
                    }

                    //LiveData
                    mViewModel.exchangeRates.postValue(apiResponse)//--> How to check the value in livedata??

                    Log.d("tvRate", "I'm gonna update the exchange rate!!")
                }
                else
                {
                    mViewModel.exchangeRates.postValue(null)
                    val errorBody: ResponseBody? = response.errorBody()
                    Log.e("API call fail", errorBody.toString())
                }
            }

            override fun onFailure(call: Call<RateXmlResponse>?, t: Throwable?) {
                Toast.makeText(applicationContext, "FAILURE: " + t.toString(), Toast.LENGTH_SHORT).show()
                Log.e("onFailure", t.toString())
                mViewModel.exchangeRates.postValue(null)
            }
        })
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
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