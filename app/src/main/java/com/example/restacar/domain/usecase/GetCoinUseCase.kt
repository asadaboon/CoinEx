package com.example.restacar.domain.usecase

import com.example.restacar.common.Result
import com.example.restacar.data.model.CoinResponse
import com.example.restacar.data.repository.GetCoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: GetCoinRepository
) {
    operator fun invoke(): Flow<Result<CoinResponse>> = flow {
        try {
            emit(Result.Success(repository.getCoinList()))
        } catch (e: HttpException) {
            emit(
                Result.Error(e.localizedMessage ?: "An unexpected error occurred")
            )
        } catch (e: IOException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}