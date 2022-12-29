package com.yasu_k.currencyconverter

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.assertj.core.api.Assertions.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class ApiResponseFetcherTest {
    val mockWebServer = MockWebServer()
    lateinit var apiResponseFetcher: ApiResponseFetcher
    var xmlResponse: RateXmlResponse? = null

    @Before
    fun setUp() {
        //Dispatchers.setMain(dispatcher)

        val dispatcher: Dispatcher = object : Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                System.out.println("request = $request")
                //return MockResponse().setResponseCode(200).setBody("Response is 200!")
                val dammyResponse = "<test>This is a response for a unit test</test>"
                System.out.println("dammyResponse = $dammyResponse")

                return MockResponse()
                    .setResponseCode(200)
                    .setBody(dammyResponse)
            }
        }
        mockWebServer.dispatcher = dispatcher
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            //.client(okHttpClient)
            .client(OkHttpClient())
            .addConverterFactory(XmlConverterFactory.create())
            .build()

        val service = retrofit.create(ApiServiceXml::class.java)
        apiResponseFetcher = ApiResponseFetcher(service)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        //Dispatchers.resetMain()
    }

    @Test
    fun getApiResponse_IsInstanceOfRateXmlResponse_and_IsNotNull() = runTest{
        xmlResponse = apiResponseFetcher.getApiResponse()

        System.out.println("xmlResponse = $xmlResponse")
        assertThat(xmlResponse).isInstanceOf(RateXmlResponse::class.java)
        assertThat(xmlResponse).isNotNull
    }

//    @Test
//    fun test_testFunc(): Unit = runBlocking{
//        val result = apiResponseFetcher.testFunc()
//        assertThat(result).isEqualTo("foo")
//    }
}