@file:JvmName("TestUtils")
package com.yasu_k.currencyconverter

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

fun getBodyFromFileName(fileName: String): String {
    val inputStream = ClassLoader.getSystemResourceAsStream(fileName)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    bufferedReader.forEachLine { buffer -> stringBuilder.append(buffer) }

    val body = stringBuilder.toString()

    return body
}