package com.example.restacar.domain.model


import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("rate")
    val rate: String,
    @SerializedName("rate_float")
    val rateFloat: Double,
    @SerializedName("symbol")
    val symbol: String,
    var image: Drawable? = null,
    var isUpdate: Boolean = false
)