package com.example.restacar.data.model

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("bpi")
    val bpi: BpiResponse,
    @SerializedName("chartName")
    val chartName: String,
    @SerializedName("disclaimer")
    val disclaimer: String,
    @SerializedName("time")
    val time: TimeResponse,
    val bpiList: ArrayList<CurrencyResponse>? = null,
)