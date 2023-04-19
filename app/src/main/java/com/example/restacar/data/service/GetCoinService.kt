package com.example.restacar.data.service

import com.example.restacar.data.model.CoinResponse
import retrofit2.http.GET

interface GetCoinService {
    @GET("bpi/currentprice.json")
    suspend fun getCoinList(): CoinResponse
}