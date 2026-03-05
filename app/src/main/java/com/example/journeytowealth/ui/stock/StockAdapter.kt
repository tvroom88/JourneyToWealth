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
    ListAdapter<MarketIndexEntity, RecyclerView.ViewHolder>(DIFF) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1

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

    // 🔥 header 때문에 +1
    override fun getItemCount(): Int = super.getItemCount() + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER
        else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_market_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_stock_row, parent, false)
            MarketIndexViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MarketIndexViewHolder) {
            val item = getItem(position - 1) // 🔥 header 보정
            holder.bind(item)
        }
    }

    // -------------------------
    // Header
    // -------------------------
    class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    // -------------------------
    // Item
    // -------------------------
    class MarketIndexViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.tvName)
        private val tvTicker: TextView = itemView.findViewById(R.id.tvTicker)
        private val tvCurrent: TextView = itemView.findViewById(R.id.tvCurrent)
        private val tvHigh: TextView = itemView.findViewById(R.id.tvHigh)
        private val tvDrawdown: TextView = itemView.findViewById(R.id.tvDrawdown)

        fun bind(item: MarketIndexEntity) {

            name.text = item.name
            tvTicker.text = item.ticker
            tvCurrent.text =
                String.format(Locale.US, "%.2f", item.currentValue)
            tvHigh.text =
                String.format(Locale.US, "%.2f", item.allTimeHigh)
            tvDrawdown.text =
                String.format(Locale.US, "%.2f%%", item.drawdownPercent * 100)

            val color =
                if (item.drawdownPercent < 0) Color.RED
                else Color.BLUE

            tvDrawdown.setTextColor(color)
        }
    }
}