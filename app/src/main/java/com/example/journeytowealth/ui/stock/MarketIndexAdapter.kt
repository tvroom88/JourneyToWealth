package com.example.journeytowealth.ui.stock

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.journeytowealth.R
import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import java.util.Locale

class MarketIndexAdapter :
    ListAdapter<MarketIndexEntity, MarketIndexAdapter.MarketIndexViewHolder>(DIFF) {

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

            val color =
                if (item.drawdownPercent < 0) Color.RED
                else Color.BLUE

            tvDrawdown.setTextColor(color)
        }
    }
}