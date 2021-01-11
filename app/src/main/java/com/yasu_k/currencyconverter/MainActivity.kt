package com.yasu_k.currencyconverter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.yasu_k.currencyconverter.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object{
        const val URL = "https://api.exchangeratesapi.io/"
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

        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val service = retrofit.create(ExchangeApiService::class.java)
        val call = service.getExchangeRate(mViewModel.currencyFrom.value, mViewModel.currencyTo.value)
        Log.d("CURRENCY", "currencyFrom: ${mViewModel.currencyFrom} currencyTo: ${mViewModel.currencyTo}")

        call.enqueue(object : Callback<RateResponse> {
            override fun onResponse(call: Call<RateResponse>?, response: Response<RateResponse>) {
                val apiResponse = response.body()

                if (response.isSuccessful) {
                    when(mViewModel.currencyTo.value){
                        "CAD" -> {
                        mViewModel.currencySymbol.value = "$"
                        mViewModel.exchangeRate.value = apiResponse.rates.CAD}

                        "USD" -> {
                        mViewModel.currencySymbol.value = "$"
                        mViewModel.exchangeRate.value = apiResponse.rates.USD}

                        "EUR" -> {
                        mViewModel.currencySymbol.value = "€"
                        mViewModel.exchangeRate.value = apiResponse.rates.EUR}

                        "JPY" -> {
                        mViewModel.currencySymbol.value = "¥"
                        mViewModel.exchangeRate.value = apiResponse.rates.JPY}

                        else -> {
                        mViewModel.exchangeRate.value = 0.0
                        mViewModel.currencySymbol.value = ""}
                    }

                    mViewModel.rateDate.value = "Update: ${apiResponse.date.toString()}"

                    //LiveData
                    mViewModel.exchangeRates.postValue(apiResponse)

                    Log.d("tvRate", "I'm gonna update the exchange rate!!")

                }else{
                    mViewModel.exchangeRates.postValue(null)
                    val errorBody: ResponseBody = response.errorBody()
                    Log.e("API call fail", errorBody.toString())
                }
            }

            override fun onFailure(call: Call<RateResponse>?, t: Throwable?) {
                Toast.makeText(applicationContext, "FAILURE !!!", Toast.LENGTH_SHORT).show()
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