package com.yasu_k.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object{
        private var currencyFrom = ""
        private var currencyTo = ""
        private lateinit var urlApi: String
        private val URL = "https://api.exchangeratesapi.io/"
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

        val tvCurrencyFrom: EditText = findViewById(R.id.tvCurrencyBeforeConversion)
        val tvCurrencyTo: TextView = findViewById(R.id.tvCurrencyAfterConversion)
        val buttonClear: Button = findViewById(R.id.buttonClear)

        buttonClear.setOnClickListener {
            tvCurrencyFrom.setText("")
            tvCurrencyTo.text = ""
        }
    }

    private fun getCurrentRate(){
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ExchangeApiService::class.java)
        val call = service.getExchangeRate(currencyFrom, currencyTo)
        Log.d("CURRENCY", "currencyFrom: $currencyFrom currencyTo: $currencyTo")

        call.enqueue(object : Callback<RateResponse> {
            override fun onResponse(call: Call<RateResponse>?, response: Response<RateResponse>) {

                var stringBuilder = "nothing"
                Toast.makeText(applicationContext, "response code: ${response.code()}", Toast.LENGTH_SHORT).show()
                if (response.code() == 200) {
                    val rateResponse = response.body()

                    stringBuilder = "real time rate: ${rateResponse.changeRate}"
                }
                Toast.makeText(applicationContext, "real time rate: $stringBuilder", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<RateResponse>?, t: Throwable?) {
                Toast.makeText(applicationContext, "FAILURE !!!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when (parent.id){
            R.id.spinnerCurrencyFrom -> {
                currencyFrom = "base=" + parent.getItemAtPosition(position)
                urlApi = URL + currencyFrom + currencyTo
                //Toast.makeText(this, "from $urlApi was selected", Toast.LENGTH_SHORT).show()

                getCurrentRate()
            }

            R.id.spinnerCurrencyTo -> {
                currencyTo = "&symbols=" + parent.getItemAtPosition(position)
                urlApi = URL + currencyFrom + currencyTo
                //Toast.makeText(this, "to $urlApi was selected", Toast.LENGTH_SHORT).show()

                getCurrentRate()
            }

            else -> Toast.makeText(this, "an error?", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nothing is selected.", Toast.LENGTH_SHORT).show()
    }
}