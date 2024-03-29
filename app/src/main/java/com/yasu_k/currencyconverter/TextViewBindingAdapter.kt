package com.yasu_k.currencyconverter

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

@BindingAdapter(value = ["currentAmount", "exchangeRate"], requireAll = true)
fun onTextChanged(textView:TextView, currentAmount: String, exchangeRate: Double){
    if (currentAmount != ""){
        if (currentAmount.isNotEmpty() || exchangeRate > 0.0){
            val decimalFormat = DecimalFormat("0.0")
            val amountAfterConv = currentAmount.toDouble() * exchangeRate
            //val amountAfterConv = getCurrentAmount(currentAmount) * exchangeRate
            textView.text = decimalFormat.format(amountAfterConv).toString()
        }
    }else{
        textView.text = ""
    }

    Log.d("BindingAdapter", "currentAmount: $currentAmount, exchangeRate: $exchangeRate")
}

@BindingAdapter("displayedExchangeRate")
fun formatDecimal(textView: TextView, exchangeRate: Double){
    val decimalFormat = DecimalFormat("0.0000")
    textView.text = decimalFormat.format(exchangeRate)
}

//fun getCurrentAmount(enteredAmount: String): Double {
//    val currentAmount: Double
//
//    enteredAmount.trim()
//    currentAmount = enteredAmount.toDouble()
//
//    return currentAmount
//}