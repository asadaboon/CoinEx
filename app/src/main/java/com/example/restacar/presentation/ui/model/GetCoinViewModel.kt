package com.example.restacar.presentation.ui.model

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restacar.common.Constants.CurrencyCodeConfig.EUR
import com.example.restacar.common.Constants.CurrencyCodeConfig.GBP
import com.example.restacar.common.Constants.CurrencyCodeConfig.USD
import com.example.restacar.common.Constants.CurrencyRateConfig.EUR_RATE
import com.example.restacar.common.Constants.CurrencyRateConfig.GBP_RATE
import com.example.restacar.common.Constants.CurrencyRateConfig.USD_RATE
import com.example.restacar.common.Resource
import com.example.restacar.domain.model.CoinResponse
import com.example.restacar.domain.model.CurrencyResponse
import com.example.restacar.domain.model.CurrencySpinnerItem
import com.example.restacar.domain.usecase.GetCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class GetCoinViewModel
@Inject
constructor(
    private val getCoinUseCase: GetCoinUseCase,
) : ViewModel() {

    var toastLivaData = MutableLiveData<String>()
    val coinLiveData = MutableLiveData<Resource<CoinResponse>>()
    val spinnerLiveData = MutableLiveData<ArrayList<CurrencySpinnerItem>>()
    val currencyRateCalculated = MutableLiveData<Pair<BigDecimal, Boolean>>()

    private lateinit var countDownTimer: CountDownTimer
    private val milliActive: Long = 1000
    private val timer: Long = 60000

    init {
        filterArray()
        generateFibonacciNumber()
        generatePrimeNumber()
    }

    fun getCoinList() {
        getCoinUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    coinLiveData.value = result
                    setSpinnerData(result.data?.bpiList)
                }
                is Resource.Error -> {
                    coinLiveData.value = result
                }
                is Resource.Loading -> {
                    coinLiveData.value = result
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUpdateCurrencyRate() {
        getCoinUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    updateCurrencyRate(result)
                }
                is Resource.Error -> {
                    setToastLivData(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    setToastLivData("isLoading")
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun setToastLivData(massage: String) {
        toastLivaData.value = massage
    }

    fun convertDateTime(date: String, inputFormat: String, outputFormat: String): String {
        return try {
            val dateFormat =
                SimpleDateFormat(inputFormat, Locale.getDefault()).parse(date) ?: Date()
            SimpleDateFormat(outputFormat, Locale.getDefault()).format(dateFormat)
        } catch (exception: Exception) {
            exception.printStackTrace()
            "-"
        }
    }

    private fun setSpinnerData(bpiList: ArrayList<CurrencyResponse>?) {
        val currencyList = ArrayList<CurrencySpinnerItem>()
        var id = 0
        bpiList?.let { currencySpinner ->
            currencySpinner.forEach { currencySpinnerItem ->
                id++
                val currencyItem = CurrencySpinnerItem(
                    id = id,
                    name = currencySpinnerItem.code,
                    image = currencySpinnerItem.image
                )
                currencyList.add(currencyItem)
            }
        }
        spinnerLiveData.value = currencyList
    }

    fun convertCurrencyToBtc(
        currencyValue: Double,
        currencyType: String,
        isClearValue: Boolean = false
    ) {
        val currency = when (currencyType) {
            USD -> {
                USD_RATE
            }
            GBP -> {
                GBP_RATE
            }
            EUR -> {
                EUR_RATE
            }
            else -> 0.0
        }
        currencyRateCalculated.value = Pair(
            formatString(currencyValue * currency),
            isClearValue
        )
    }

    private fun formatString(multiplyValue: Double): BigDecimal {
        return BigDecimal(multiplyValue).toEngineeringString().toBigDecimal()
    }

    fun autoUpdateCurrencyRate() {
        countDownTimer = object : CountDownTimer(timer, milliActive) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                getUpdateCurrencyRate()
                countDownTimer.start()
            }
        }
        countDownTimer.start()
    }

    private fun updateCurrencyRate(result: Resource.Success<CoinResponse>) {
        result.data?.bpiList?.forEach { currencyResponse ->
            currencyResponse.isUpdate = true
        }
        //To update rate.
        coinLiveData.value = result
    }

    fun cancelTimer() {
        countDownTimer.cancel()
    }

    /**
     * Filter Array not use map, filter, contain.
     * **/
    private fun filterArray() {
        val intArr1 = arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        val intArr2 = arrayOf(4, 10, 2, 12, 13, 8, 15, 16)

        intArr1.forEach { int1 ->
            intArr2.forEach { int2 ->
                if (int1 == int2) {
                    Log.e("ASADA", "filterArray: int1 = $int1 int2 = $int2")
                }
            }
        }
    }

    /**
     * generate Prime number.
     * **/
    private fun generatePrimeNumber() {
        var initialNumber = 2
        val maxGenerateNumber = 20
        Log.e("ASADA", "The value of N is defined as $maxGenerateNumber")

        while (initialNumber < maxGenerateNumber) {
            if (primeNumber(initialNumber))
                Log.e("ASADA", "prime numbers is: $initialNumber")
            ++initialNumber
        }
    }

    private fun primeNumber(num: Int): Boolean {
        var isPrimeNumber = true
        for (i in 2..num / 2) {
            if (num % i == 0) {
                isPrimeNumber = false
                break
            }
        }
        return isPrimeNumber
    }

    /**
     * generate Fibonacci number.
     * **/
    private fun generateFibonacciNumber() {
        var i = 1
        val n = 20
        var fibo1 = 0 //applies only to the first integer.
        var fibo2 = 1 //applies only to the second integer.
        Log.e("ASADA,", "First $n terms: ")

        while (i <= n) {
            Log.e("ASADA,", "Fibonacci : $fibo1 ")
            val fiboN = fibo1 + fibo2
            fibo1 = fibo2
            fibo2 = fiboN
            i++
        }
    }
}
