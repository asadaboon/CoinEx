package com.example.restacar.domain.repository

import com.example.restacar.domain.model.CoinResponse

interface GetCoinRepository {
    suspend fun getCoinList(): CoinResponse
}