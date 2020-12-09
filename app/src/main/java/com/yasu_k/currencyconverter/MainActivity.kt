    package com.yasu_k.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
        private var currencyFrom = ""
        private var currencyTo = ""
        const val URL = "https://api.exchangeratesapi.io/"
        private lateinit var tvRate: TextView
        private lateinit var tvCurrencyTo: TextView
        private lateinit var etCurrencyFrom: EditText
        private lateinit var tvRealRate: TextView
        private lateinit var tvCurrency: TextView

        var exchangeRates: MutableLiveData<RateResponse> = MutableLiveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        etCurrencyFrom = findViewById(R.id.etCurrencyBeforeConversion)
        tvCurrencyTo = findViewById(R.id.tvCurrencyAfterConversion)
        val buttonClear: Button = findViewById(R.id.buttonClear)

        tvCurrency = findViewById(R.id.tvCurrency)

        buttonClear.setOnClickListener {
            etCurrencyFrom.setText("")
            tvCurrencyTo.text = ""
        }

        etCurrencyFrom.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                convertCurrency()
            }
        })

        setupObservers()

        tvRate = findViewById(R.id.tvRate)
        tvRealRate = findViewById(R.id.tvRealRate)
    }

    private fun setupObservers(){
        exchangeRates.observe(this, Observer {
            // this code is called whenever value of exchangeRates changes
            convertCurrency()
        })
    }

    private fun convertCurrency(){
        Log.d("convertCurrency()", "Beginning of the conversion!!")
        val currencyToConvert = etCurrencyFrom.text.toString()
        val exchangeRate = tvRate.text.toString()

        if (currencyToConvert != ""){
            val convertedCurrency: Double = currencyToConvert.toDouble() * exchangeRate.toDouble()
            val decimalFormat = DecimalFormat("0.0")
            tvCurrencyTo.text = decimalFormat.format(convertedCurrency)
        }
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
        val call = service.getExchangeRate(currencyFrom, currencyTo)
        Log.d("CURRENCY", "currencyFrom: $currencyFrom currencyTo: $currencyTo")

        call.enqueue(object : Callback<RateResponse> {
            override fun onResponse(call: Call<RateResponse>?, response: Response<RateResponse>) {
                val realRate: Double
                val decimalFormat = DecimalFormat("0.0000")
                val apiResponse = response.body()

                val dateRate: String

                if (response.isSuccessful) {

                    /*realRate = when(currencyTo){
                        "CAD" -> apiResponse.rates.CAD
                        "USD" -> apiResponse.rates.USD
                        "EUR" -> apiResponse.rates.EUR
                        "JPY" -> apiResponse.rates.JPY
                        else -> 0.0
                    }*/
                    when(currencyTo){
                        "CAD" -> {realRate = apiResponse.rates.CAD
                        tvCurrency.text = "$"}
                        "USD" -> {realRate = apiResponse.rates.USD
                        tvCurrency.text = "$"}
                        "EUR" -> {realRate = apiResponse.rates.EUR
                        tvCurrency.text = "€"}
                        "JPY" -> {realRate = apiResponse.rates.JPY
                        tvCurrency.text = "¥"}
                        else -> {realRate = 0.0
                        tvCurrency.text = ""}
                    }

                    dateRate = "Update: ${apiResponse.date.toString()}"

                    //LiveData
                    exchangeRates.postValue(apiResponse)

                    Log.d("tvRate", "I'm gonna update the exchange rate!!")
                    tvRate.text = decimalFormat.format(realRate)

                    tvRealRate.text = dateRate

                }else{
                    exchangeRates.postValue(null)
                    val errorBody: ResponseBody = response.errorBody()
                    Log.e("API call fail", errorBody.toString())
                }
            }

            override fun onFailure(call: Call<RateResponse>?, t: Throwable?) {
                Toast.makeText(applicationContext, "FAILURE !!!", Toast.LENGTH_SHORT).show()
                exchangeRates.postValue(null)
            }
        })
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when (parent.id){
            R.id.spinnerCurrencyFrom -> {
                currencyFrom = "" + parent.getItemAtPosition(position)
                getCurrentRate()
            }

            R.id.spinnerCurrencyTo -> {
                currencyTo = "" + parent.getItemAtPosition(position)
                getCurrentRate()
            }

            else -> Log.d("onItemSelected", "An error occurred")
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}