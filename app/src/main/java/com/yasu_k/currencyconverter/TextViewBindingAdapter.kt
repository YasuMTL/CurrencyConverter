package com.yasu_k.currencyconverter

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

@BindingAdapter(value = ["app:currentAmount", "app:exchangeRate"], requireAll = true)
fun onTextChanged(textView:TextView, currentAmount: String, exchangeRate: Double){
    if (currentAmount != ""){
        if (currentAmount.isNotEmpty() || exchangeRate > 0.0){
            val decimalFormat = DecimalFormat("0.0")
            val amountAfterConv = currentAmount.toDouble() * exchangeRate
            textView.text = decimalFormat.format(amountAfterConv).toString()
        }
    }

    Log.d("BindingAdapter", "currentAmount: $currentAmount, exchangeRate: $exchangeRate")
}

@BindingAdapter("app:displayedExchangeRate")
fun formatDecimal(textView: TextView, exchangeRate: Double){
    val decimalFormat = DecimalFormat("0.0000")
    textView.text = decimalFormat.format(exchangeRate)
}