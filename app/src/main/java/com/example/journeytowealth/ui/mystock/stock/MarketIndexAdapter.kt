package com.example.journeytowealth.ui.mystock.stock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.journeytowealth.R
import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import java.util.Locale

class MarketIndexAdapter :
    androidx.recyclerview.widget.ListAdapter<MarketIndexEntity, MarketIndexAdapter.MarketIndexViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<MarketIndexEntity>() {
            override fun areItemsTheSame(
                oldItem: MarketIndexEntity,
                newItem: MarketIndexEntity
            ): Boolean = oldItem.ticker == newItem.ticker

            override fun areContentsTheSame(
                oldItem: MarketIndexEntity,
                newItem: MarketIndexEntity
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketIndexViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock_row, parent, false)

        return MarketIndexViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarketIndexViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MarketIndexViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.tvTicker)

        //        private val tvTicker: TextView = itemView.findViewById(R.id.tvTicker)
        private val tvCurrent: TextView = itemView.findViewById(R.id.tvCurrent)
        private val tvHigh: TextView = itemView.findViewById(R.id.tvHigh)
        private val tvDrawdown: TextView = itemView.findViewById(R.id.tvDrawdown)

        fun bind(item: MarketIndexEntity) {

            name.text = item.ticker

            tvCurrent.text =
                String.format(Locale.US, "%.2f", item.currentValue)

            tvHigh.text =
                String.format(Locale.US, "%.2f", item.allTimeHigh)

            tvDrawdown.text =
                String.format(Locale.US, "%.2f%%", item.drawdownPercent)

            val color = getDrawdownColor(item.drawdownPercent)
            tvDrawdown.setTextColor(color)
        }

        fun getDrawdownColor(rate: Float): Int {
            return when {
                rate >= -5 -> "#D32F2F".toColorInt()
                rate >= -15 -> "#EF5350".toColorInt()
                rate >= -30 -> "#64B5F6".toColorInt()
                rate >= -50 -> "#64B5F6".toColorInt()
                else -> "#1976D2".toColorInt()
            }
        }
    }
}