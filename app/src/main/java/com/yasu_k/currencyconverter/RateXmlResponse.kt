package com.yasu_k.currencyconverter

data class RateXmlResponse
(
    var currencyRates: List<CurrencyRate>? = null
)
{
    fun getRate(currency_to_check: String): Double
    {
        if (currencyRates != null)
        {
            for (aRate in currencyRates!!)
            {
                if (currency_to_check == aRate.currency)
                {
                    return aRate.rate
                }
            }
        }

        return 0.0
    }
}

data class CurrencyRate
(
    var currency: String,
    var rate: Double
)