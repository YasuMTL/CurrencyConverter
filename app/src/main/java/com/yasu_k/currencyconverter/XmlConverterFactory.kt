package com.yasu_k.currencyconverter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class XmlConverterFactory private constructor(): Converter.Factory()
{
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> = XmlResponseConverter()
//    {
//        return super.responseBodyConverter(type, annotations, retrofit)
//    }

    companion object{
        fun create() = XmlConverterFactory()
    }
}