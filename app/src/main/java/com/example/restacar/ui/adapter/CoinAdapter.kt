package com.example.restacar.ui.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restacar.R
import com.example.restacar.data.model.CurrencyResponse
import com.example.restacar.databinding.ItemCoinBinding


class CoinAdapter(private val item: ArrayList<CurrencyResponse>) :
    RecyclerView.Adapter<CoinAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size

    class ViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val mContext: Context = binding.root.context
        fun bind(item: CurrencyResponse) {
            binding.apply {
                coinCodeNameTextView.text = item.code
                coinRateTextView.text = item.rate
                Glide.with(itemView.context)
                    .load(item.image)
                    .placeholder(R.drawable.ic_icon_usd)
                    .error(R.drawable.ic_icon_usd)
                    .into(coinImageView)

                if (item.isUpdate) {
                    blinkText(isUpdate = true)
                    Handler(Looper.getMainLooper()).postDelayed({
                        blinkText(isUpdate = false)
                    }, 5000)
                }
            }
        }

        private fun blinkText(isUpdate: Boolean) {
            binding.apply {
                val animator =
                    AnimationUtils.loadAnimation(mContext, R.anim.text_blink_anim)
                coinRateTextView.apply {
                    if (isUpdate) {
                        setTextColor(mContext.getColor(R.color.red))
                        startAnimation(animator)
                    } else {
                        setTextColor(mContext.getColor(R.color.black))
                        clearAnimation()
                    }
                }
            }
        }
    }
}