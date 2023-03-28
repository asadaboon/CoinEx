package com.example.restacar.domain.usecase

import com.example.restacar.R
import com.example.restacar.common.AppResourceProvider
import com.example.restacar.common.Constants.CurrencyCodeConfig.EUR
import com.example.restacar.common.Constants.CurrencyCodeConfig.GBP
import com.example.restacar.common.Constants.CurrencyCodeConfig.USD
import com.example.restacar.common.Resource
import com.example.restacar.domain.model.BpiResponse
import com.example.restacar.domain.model.CoinResponse
import com.example.restacar.domain.model.CurrencyResponse
import com.example.restacar.domain.repository.GetCoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: GetCoinRepository,
    private val resourceProvider: AppResourceProvider
) {
    operator fun invoke(): Flow<Resource<CoinResponse>> = flow {
        try {
            emit(Resource.Success(mapData(repository.getCoinList())))
        } catch (e: HttpException) {
            emit(
                Resource.Error(e.localizedMessage ?: "An unexpected error occured")
            )
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    private fun mapData(jsonObject: CoinResponse): CoinResponse {
        return CoinResponse(
            bpi = jsonObject.bpi,
            chartName = jsonObject.chartName,
            time = jsonObject.time,
            disclaimer = jsonObject.disclaimer,
            bpiList = mapBpiList(bpiList = jsonObject.bpi)
        )
    }

    private fun mapBpiList(bpiList: BpiResponse): ArrayList<CurrencyResponse> {
        val bpiArrayList = ArrayList<CurrencyResponse>()
        bpiArrayList.apply {
            add(bpiList.eUR)
            add(bpiList.gBP)
            add(bpiList.uSD)
        }
        mapCurrencyImage(bpiArrayList)
        return bpiArrayList
    }

    private fun mapCurrencyImage(bpiArrayList: ArrayList<CurrencyResponse>) {
        bpiArrayList.forEach { currencyResponse ->
            when (currencyResponse.code) {
                USD -> {
                    currencyResponse.image =
                        resourceProvider.getDrawable(R.drawable.ic_icon_usd)
                }
                GBP -> {
                    currencyResponse.image =
                        resourceProvider.getDrawable(R.drawable.ic_icon_gbp)
                }
                EUR -> {
                    currencyResponse.image =
                        resourceProvider.getDrawable(R.drawable.ic_icon_eur)
                }
            }
        }
    }
}