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

//https://stackoverflow.com/questions/60686547/how-to-parse-xml-response-using-retrofit-in-kotlin
/*
@Root(name = "gesmes:Envelope" , strict = false)
data class RateXmlResponse //@JvmOverloads constructor
(
    @field:Element(name = "gesmes:subject", required = false)
    var subject: Currencies,

    @field:Element(name = "gesmes:Sender", required = false)
    var sender: Currencies,

    @field:Element(name = "Cube", required = false)
    var cube: Cube
)
@Root(name = "Cube" , strict = false)
//data class CubeToday @JvmOverloads constructor
data class Cube @JvmOverloads constructor
(
    @field:ElementList(inline = true, required = false, entry = "Cube")
    var cube: ArrayList<Currencies>? = null,

    @Attribute
    var time:String = ""
)

@Root(name = "Cube", strict = false)
data class Currencies(
        @Attribute
        var currency: String,
        @Attribute
        var rate: String
)
*/

//data class Currencies(
//data class Club(
//    var CAD: Double,
//    var USD: Double,
//    var EUR: Double,
//    var JPY: Double
//)
