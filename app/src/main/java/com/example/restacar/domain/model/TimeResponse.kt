package com.example.restacar.domain.model


import com.google.gson.annotations.SerializedName

data class TimeResponse(
    @SerializedName("updated")
    val updated: String,
    @SerializedName("updatedISO")
    val updatedISO: String,
    @SerializedName("updateduk")
    val updateduk: String
)