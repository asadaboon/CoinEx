package com.example.restacar.common

object Constants {

    const val BASE_URL = "https://api.coindesk.com/v1/"

    object DateTimeConfig {
        const val DATE_TIME_PATTERN_API_NO_TIMEZONE = "MMM d, yyyy hh:mm:ss"
        const val DATE_PATTERN_FULL_MONTH_SPACE = "d MMMM yyyy"
    }

    object CurrencyCodeConfig {
        const val USD = "USD"
        const val GBP = "GBP"
        const val EUR = "EUR"
    }

    object CurrencyRateConfig {
        const val USD_RATE = 0.000033
        const val GBP_RATE = 0.000044
        const val EUR_RATE = 0.000038
    }
}