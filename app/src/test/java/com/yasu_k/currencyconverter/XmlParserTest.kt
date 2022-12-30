package com.yasu_k.currencyconverter

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.assertj.core.api.Assertions.*

class XmlParserTest {
    private lateinit var bodyString: String
    lateinit var response: RateXmlResponse
    val parser = XmlParser()

    @Before
    fun setUp() {
        bodyString = getBodyFromFileName("test.xml")
        System.out.println("bodyString = $bodyString")
    }

    @After
    fun tearDown() {
    }

    @Test
    fun parse() {
        response = parser.parse(bodyString.byteInputStream())
        assertThat(response).isNotNull
    }
}