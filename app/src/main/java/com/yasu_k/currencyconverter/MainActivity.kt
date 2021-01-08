package com.yasu_k.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object{
        const val URL = "https://api.exchangeratesapi.io/"
        private lateinit var tvRate: TextView
        private lateinit var tvRealRate: TextView
        private lateinit var tvCurrency: TextView

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

        tvCurrency = findViewById(R.id.tvCurrency)

        setupObservers()

        tvRate = findViewById(R.id.tvRate)
        tvRealRate = findViewById(R.id.tvRealRate)
    }

    private fun setSpinners(){
        val spinnerCurrencyFrom: Spinner = findViewById(R.id.spinnerCurrencyFrom)
        val spinnerCurrencyTo: Spinner = findViewById(R.id.spinnerCurrencyTo)

        ArrayAdapter.createFromResource(
                this,
                R.array.currency_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCurrencyFrom.adapter = adapter
            spinnerCurrencyTo.adapter = adapter
        }

        spinnerCurrencyFrom.onItemSelectedListener = this
        spinnerCurrencyTo.onItemSelectedListener = this
    }

    private fun setupObservers(){
        mViewModel.exchangeRates.observe(this, Observer {
            // this code is called whenever value of exchangeRates changes
            //convertCurrency()
        })
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
        val call = service.getExchangeRate(mViewModel.currencyFrom, mViewModel.currencyTo)
        Log.d("CURRENCY", "currencyFrom: ${mViewModel.currencyFrom} currencyTo: ${mViewModel.currencyTo}")

        call.enqueue(object : Callback<RateResponse> {
            override fun onResponse(call: Call<RateResponse>?, response: Response<RateResponse>) {
                val realRate: Double
                val decimalFormat = DecimalFormat("0.0000")
                val apiResponse = response.body()

                val dateRate: String

                if (response.isSuccessful) {
                    when(mViewModel.currencyTo){
                        "CAD" -> {realRate = apiResponse.rates.CAD
                        tvCurrency.text = "$"
                        //To keep and use the value of the exchange rate in the XML file
                        //mViewModel.exchangeRate = apiResponse.rates.CAD}
                        mViewModel.exchangeRate.value = apiResponse.rates.CAD}

                        "USD" -> {realRate = apiResponse.rates.USD
                        tvCurrency.text = "$"
                        //mViewModel.exchangeRate = apiResponse.rates.USD}
                        mViewModel.exchangeRate.value = apiResponse.rates.USD}

                        "EUR" -> {realRate = apiResponse.rates.EUR
                        tvCurrency.text = "€"
                        //mViewModel.exchangeRate = apiResponse.rates.EUR}
                        mViewModel.exchangeRate.value = apiResponse.rates.EUR}

                        "JPY" -> {realRate = apiResponse.rates.JPY
                        tvCurrency.text = "¥"
                        //mViewModel.exchangeRate = apiResponse.rates.JPY}
                        mViewModel.exchangeRate.value = apiResponse.rates.JPY}

                        else -> {realRate = 0.0
                        mViewModel.exchangeRate.value = 0.0
                        tvCurrency.text = ""}
                    }

                    dateRate = "Update: ${apiResponse.date.toString()}"

                    //LiveData
                    mViewModel.exchangeRates.postValue(apiResponse)

                    Log.d("tvRate", "I'm gonna update the exchange rate!!")
                    tvRate.text = decimalFormat.format(realRate)

                    tvRealRate.text = dateRate

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
                mViewModel.currencyFrom = "" + parent.getItemAtPosition(position)
                getCurrentRate()
            }

            R.id.spinnerCurrencyTo -> {
                mViewModel.currencyTo = "" + parent.getItemAtPosition(position)
                getCurrentRate()
            }

            else -> Log.d("onItemSelected", "An error occurred")
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}