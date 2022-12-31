package com.yasu_k.currencyconverter

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class XmlParser
{
    fun parse(input: InputStream): RateXmlResponse
    {
        val rateResponses = RateXmlResponse()

        val rates = mutableListOf<CurrencyRate>()

        try
        {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true

            val parser = factory.newPullParser()
            parser.setInput(input, null)

            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                val tagName = parser.name
                var currency:String
                var rate: Double

                when (eventType)
                {
                    XmlPullParser.END_TAG -> if (tagName.equals("cube", ignoreCase = true))
                    {
                        try
                        {
                            if(!parser.getAttributeValue(null, "currency").isNullOrEmpty() )
                            {
                                val rateResponse = CurrencyRate("", 0.0)
                                currency = parser.getAttributeValue(null, "currency")
                                rate = (parser.getAttributeValue(null, "rate") ).toDouble()

                                rateResponse.currency = currency
                                rateResponse.rate = rate

                                rates.add(rateResponse)

                                rateResponses.currencyRates
                            }
                        }
                        catch (e: XmlPullParserException)
                        {
                            e.printStackTrace()
                        }
                        catch (e: IOException)
                        {
                            e.printStackTrace()
                        }
                    }
                }

                eventType = parser.next()
            }

            rateResponses.currencyRates = rates
        }
        catch (e: XmlPullParserException)
        {
            e.printStackTrace()
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }

        return rateResponses
    }
}