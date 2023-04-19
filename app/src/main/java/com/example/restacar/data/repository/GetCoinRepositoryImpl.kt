package com.example.restacar.data.repository

import com.example.restacar.R
import com.example.restacar.common.AppResourceProvider
import com.example.restacar.common.Constants
import com.example.restacar.data.model.BpiResponse
import com.example.restacar.data.model.CoinResponse
import com.example.restacar.data.model.CurrencyResponse
import com.example.restacar.data.service.GetCoinService
import javax.inject.Inject

class GetCoinRepositoryImpl @Inject constructor(
    private val service: GetCoinService,
    private val resourceProvider: AppResourceProvider
) :
    GetCoinRepository {
    override suspend fun getCoinList(): CoinResponse {
        return mapData(service.getCoinList())
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
                Constants.CurrencyCodeConfig.USD -> {
                    currencyResponse.image =
                        resourceProvider.getDrawable(R.drawable.ic_icon_usd)
                }
                Constants.CurrencyCodeConfig.GBP -> {
                    currencyResponse.image =
                        resourceProvider.getDrawable(R.drawable.ic_icon_gbp)
                }
                Constants.CurrencyCodeConfig.EUR -> {
                    currencyResponse.image =
                        resourceProvider.getDrawable(R.drawable.ic_icon_eur)
                }
            }
        }
    }
}