package com.example.restacar

import com.example.restacar.common.JsonFactory
import com.example.restacar.data.remote.repository.GetCoinRepositoryImpl
import com.example.restacar.data.service.GetCoinService
import com.example.restacar.domain.model.CoinResponse
import com.example.restacar.domain.usecase.GetCoinUseCase
import com.example.restacar.presentation.ui.model.GetCoinViewModel
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(JUnit4::class)
class GetCoinRepositoryImplTest {

    private lateinit var service: GetCoinService
    private lateinit var remote: GetCoinRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private val getCoinUseCase = mock(GetCoinUseCase::class.java)
    private lateinit var viewModel: GetCoinViewModel

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GetCoinService::class.java)

        remote = GetCoinRepositoryImpl(service)
    }

    @Test
    suspend fun getCoinList() {
        enqueueResponse("get_coins_list_success")
        val fetch = remote.getCoinList()

        val request = mockWebServer.takeRequest()
        MatcherAssert.assertThat(
            request.path,
            CoreMatchers.`is`("bpi/currentprice.json")
        )
        MatcherAssert.assertThat(fetch, IsNull.notNullValue())
        MatcherAssert.assertThat(
            fetch,
            CoreMatchers.`is`(CoreMatchers.instanceOf(CoinResponse::class.java))
        )
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val fromJson = JsonFactory.getStringFromJsonTestResource("coins/$fileName") ?: ""
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(fromJson)
        )
    }


    /*   @Test
       fun convertCurrencyToBtcTest() {
           val expectedCurrencyValue = 2700.0
           val expectedCurrencyType = "USD"
           val expectedIsClearValue = false
           val convert = viewModel.convertCurrencyToBtc(
               currencyValue = expectedCurrencyValue,
               currencyType = expectedCurrencyType,
               isClearValue = expectedIsClearValue
           )
           Assert.assertNotNull(convert)
       }*/
}

