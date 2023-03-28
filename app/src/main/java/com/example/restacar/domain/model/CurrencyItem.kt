package com.example.restacar.domain.model

import android.graphics.drawable.Drawable

data class CurrencySpinnerItem(
    var id: Int = 0,
    var name: String = "",
    var image: Drawable? = null,
) {
    override fun toString(): String {
        return name
    }
}