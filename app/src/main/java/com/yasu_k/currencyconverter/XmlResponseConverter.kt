package com.yasu_k.currencyconverter

import okhttp3.ResponseBody
import retrofit2.Converter

class XmlResponseConverter: Converter<ResponseBody, RateXmlResponse>
{
    private val parser = XmlParser()

    override fun convert(responseBody: ResponseBody): RateXmlResponse
    {
        lateinit var response: RateXmlResponse

        val bodyString = responseBody.string()
        response = parser.parse(bodyString.byteInputStream())

        return response
    }
}