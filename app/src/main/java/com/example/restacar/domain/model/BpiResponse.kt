package com.example.restacar.domain.model


import com.google.gson.annotations.SerializedName

open class BpiResponse(
    @SerializedName("EUR")
    val eUR: CurrencyResponse,
    @SerializedName("GBP")
    val gBP: CurrencyResponse,
    @SerializedName("USD")
    val uSD: CurrencyResponse
)