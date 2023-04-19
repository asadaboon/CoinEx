package com.example.restacar.ui.viewModel

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
import com.example.restacar.common.Result
import com.example.restacar.data.model.CoinResponse
import com.example.restacar.data.model.CurrencyResponse
import com.example.restacar.data.model.CurrencySpinnerItem
import com.example.restacar.domain.usecase.GetCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class GetCoinViewModel
@Inject
constructor(
    private val getCoinUseCase: GetCoinUseCase,
) : ViewModel() {

    val toastLivaData = MutableLiveData<String>()
    val coinLiveData = MutableLiveData<Result<CoinResponse>>()
    val spinnerLiveData = MutableLiveData<ArrayList<CurrencySpinnerItem>>()
    val currencyRateCalculated = MutableLiveData<Pair<BigDecimal, Boolean>>()

    private lateinit var countDownTimer: CountDownTimer
    private val milliActive: Long = 1000
    private val timer: Long = 60000

    init {
        filterArray()
        generateFibonacciNumber()
        generatePrimeNumber()
        for (number: Int in 1..3) {
            Log.e("KOTLIN", " $number")
        }
    }

    fun getCoinList() {
/*        getCoinUseCase().onEach { result ->
            when (result) {
                is Result.Success -> {
                    coinLiveData.value = result
                    setSpinnerData(result.data?.bpiList)
                }
                is Result.Error -> {
                    coinLiveData.value = result
                }
                is Result.Loading -> {
                    coinLiveData.value = result
                }
            }
        }.launchIn(viewModelScope)*/
        getCoinUseCase().onStart {
            Log.e("BOONTHAM", "getCoinList: onStart")
        }.onEach { result ->
            Log.e("BOONTHAM", "getCoinList: onEach")
            when (result) {
                is Result.Success -> {
                    coinLiveData.value = result
                    setSpinnerData(result.data?.bpiList)
                }
                is Result.Error -> {
                    coinLiveData.value = result
                }
                is Result.Loading -> {
                    coinLiveData.value = result
                }
            }
        }.onCompletion {
            Log.e("BOONTHAM", "getCoinList: onCompletion")
        }.launchIn(viewModelScope)
    }

    fun getUpdateCurrencyRate() {
        getCoinUseCase().onEach { result ->
            when (result) {
                is Result.Success -> {
                    updateCurrencyRate(result)
                }
                is Result.Error -> {
                    setToastLivData(result.message ?: "An unexpected error occurred")
                }
                is Result.Loading -> {
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
        val currencyRate = when (currencyType) {
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
            formatString(currencyValue * currencyRate),
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

    private fun updateCurrencyRate(result: Result.Success<CoinResponse>) {
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
            if (num % 2 == 0) {
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
        var fibo1 = 1 //applies only to the first integer.
        var fibo2 = 1 //applies only to the second integer.
        Log.e("ASADA,", "First $n terms: ")

        while (i <= n) {
            Log.e("ASADA,", "Fibonacci : $fibo1")
            val fiboN = fibo1 + fibo2
            fibo1 = fibo2
            fibo2 = fiboN
            i++
        }
    }
}
