package com.yasu_k.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var currencyFrom = ""
    private var currencyTo = ""
    private lateinit var uriApi: String
    private val URL = "https://api.exchangeratesapi.io/latest"

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

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when (parent.id){
            R.id.spinnerCurrencyFrom -> {
                currencyFrom = "?base=" + parent.getItemAtPosition(position)
                uriApi = URL + currencyFrom + currencyTo
                Toast.makeText(this, "from $uriApi was selected", Toast.LENGTH_SHORT).show()
            }

            R.id.spinnerCurrencyTo -> {
                currencyTo = "&symbols=" + parent.getItemAtPosition(position)
                uriApi = URL + currencyFrom + currencyTo
                Toast.makeText(this, "to $uriApi was selected", Toast.LENGTH_SHORT).show()
            }

            else -> Toast.makeText(this, "an error?", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nothing is selected.", Toast.LENGTH_SHORT).show()
    }
}