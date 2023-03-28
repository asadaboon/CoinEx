package com.example.restacar.presentation.ui.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restacar.R
import com.example.restacar.common.ApiStatus
import com.example.restacar.common.CheckInternetConnectionUtils
import com.example.restacar.common.Constants
import com.example.restacar.common.Resource
import com.example.restacar.databinding.ActivityMainBinding
import com.example.restacar.domain.model.CoinResponse
import com.example.restacar.presentation.ui.adapter.SpinnerAdapter
import com.example.restacar.presentation.ui.adapter.CoinAdapter
import com.example.restacar.presentation.ui.model.GetCoinViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GetCoinViewModel by viewModels()
    private lateinit var spinnerAdapter: SpinnerAdapter
    private var currency = ""
    private var currencyValue = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setObserve()
        setUpView()
    }

    private fun initView() {
        if (CheckInternetConnectionUtils().checkInternetConnection(this)) {
            viewModel.getCoinList()
            viewModel.autoUpdateCurrencyRate()
        } else {
            binding.apply {
                contentLayout.visibility = View.GONE
                connectFailLayout.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setObserve() {
        viewModel.coinLiveData.observe(this) { result ->
            binding.apply {
                result.message
                when (result.status) {
                    ApiStatus.LOADING -> {
                        currencyShimmerLayout.startShimmer()
                    }
                    ApiStatus.SUCCESS -> {
                        contentLayout.visibility = View.VISIBLE
                        emptyLayout.visibility = View.GONE
                        connectFailLayout.visibility = View.GONE
                        swipeRefreshLayout.isRefreshing = false
                        setUpItemView(result)
                    }
                    ApiStatus.ERROR -> {
                        contentLayout.visibility = View.GONE
                        emptyLayout.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }

        viewModel.spinnerLiveData.observe(this) { currencySpinner ->
            spinnerAdapter = SpinnerAdapter(this@MainActivity, currencySpinner)
            binding.currencySpinner.adapter = spinnerAdapter
            binding.currencySpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        p3: Long
                    ) {
                        currency = currencySpinner[position].name

                        viewModel.convertCurrencyToBtc(
                            currencyValue = currencyValue,
                            currencyType = currency
                        )
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        // Do nothing.
                    }
                }
        }

        viewModel.currencyRateCalculated.observe(this) { currencyRateCalculatedPairData ->
            binding.apply {
                btcEditText.setText(currencyRateCalculatedPairData.first.toString())
                if (currencyRateCalculatedPairData.second) {
                    currencyEditText.setText("")
                }
            }
        }
    }

    private fun setUpItemView(result: Resource<CoinResponse>) {
        binding.apply {
            result.data?.bpiList?.let { currencyList ->
                val coinAdapter = CoinAdapter(item = currencyList)
                val updateAt = viewModel.convertDateTime(
                    date = result.data.time.updated,
                    inputFormat = Constants.DateTimeConfig.DATE_TIME_PATTERN_API_NO_TIMEZONE,
                    outputFormat = Constants.DateTimeConfig.DATE_PATTERN_FULL_MONTH_SPACE
                )
                currencyUpdateDateTextView.text = getString(
                    R.string.update_at, updateAt
                )
                currencyShimmerLayout.stopShimmer()
                currencyShimmerLayout.visibility = View.GONE
                currencyRecyclerView.apply {
                    layoutManager = LinearLayoutManager(
                        context, LinearLayoutManager.VERTICAL, false
                    )
                    adapter = coinAdapter
                    coinAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            clearImageView.setOnClickListener {
                viewModel.convertCurrencyToBtc(
                    currencyValue = 0.00,
                    currencyType = "",
                    isClearValue = true
                )
            }

            exchangeImageView.setOnClickListener {

            }

            currencyEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    //do nothing.
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                    //do nothing.
                }

                override fun onTextChanged(
                    str: CharSequence, start: Int, before: Int, count: Int
                ) {
                    currencyValue = if (str.isEmpty()) 0.00 else str.toString().toDouble()

                    if (str.isNotEmpty()) {
                        viewModel.convertCurrencyToBtc(
                            currencyValue = str.toString().toDouble(),
                            currencyType = currency
                        )
                    } else {
                        viewModel.convertCurrencyToBtc(
                            currencyValue = 0.00,
                            currencyType = ""
                        )
                    }
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                if (CheckInternetConnectionUtils().checkInternetConnection(this@MainActivity)) {
                    swipeRefreshLayout.isRefreshing = true
                    viewModel.getCoinList()
                } else {
                    contentLayout.visibility = View.GONE
                    connectFailLayout.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelTimer()
    }
}