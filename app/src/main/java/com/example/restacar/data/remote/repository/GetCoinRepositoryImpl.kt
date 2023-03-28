package com.example.restacar.data.remote.repository

import com.example.restacar.domain.model.CoinResponse
import com.example.restacar.data.service.GetCoinService
import com.example.restacar.domain.repository.GetCoinRepository
import javax.inject.Inject

class GetCoinRepositoryImpl @Inject constructor(private val service: GetCoinService) :
    GetCoinRepository {
    override suspend fun getCoinList(): CoinResponse {
        return service.getCoinList()
    }
}