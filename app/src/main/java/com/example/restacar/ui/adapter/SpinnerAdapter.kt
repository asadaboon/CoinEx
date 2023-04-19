package com.example.restacar.ui.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.restacar.R
import com.example.restacar.data.model.CurrencySpinnerItem

class SpinnerAdapter(
    private val activity: Activity,
    private val item: ArrayList<CurrencySpinnerItem>
) :
    ArrayAdapter<CurrencySpinnerItem>(activity, R.layout.item_spinner) {
    override fun getCount(): Int {
        return item.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return bindView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return bindView(position, parent)
    }

    private fun bindView(position: Int, parent: ViewGroup): View {
        val context = activity.layoutInflater
        val rootView = context.inflate(R.layout.item_spinner, parent, false)
        val coinCodeNameTextView = rootView.findViewById<TextView>(R.id.coinCodeNameTextView)
        val coinImageView = rootView.findViewById<ImageView>(R.id.coinImageView)

        coinCodeNameTextView.text = item[position].name
        Glide.with(rootView.context)
            .load(item[position].image)
            .placeholder(R.drawable.ic_icon_usd)
            .error(R.drawable.ic_icon_usd)
            .into(coinImageView)

        return rootView
    }
}
