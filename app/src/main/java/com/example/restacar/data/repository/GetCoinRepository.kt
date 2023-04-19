package com.example.restacar.data.repository

import com.example.restacar.data.model.CoinResponse

interface GetCoinRepository {
    suspend fun getCoinList(): CoinResponse
}