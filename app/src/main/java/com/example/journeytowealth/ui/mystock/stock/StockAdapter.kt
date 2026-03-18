package com.example.journeytowealth.ui.mystock.stock

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.journeytowealth.R
import com.example.journeytowealth.data.local.entity.StockEntity
import java.util.Locale

class StockAdapter :
    androidx.recyclerview.widget.ListAdapter<StockEntity, StockAdapter.StockViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<StockEntity>() {

            override fun areItemsTheSame(
                oldItem: StockEntity,
                newItem: StockEntity
            ): Boolean = oldItem.ticker == newItem.ticker

            override fun areContentsTheSame(
                oldItem: StockEntity,
                newItem: StockEntity
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock_row, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: StockViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class StockViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.tvTicker)

        //        private val ticker: TextView = itemView.findViewById(R.id.tvTicker)
        private val tvCurrent: TextView = itemView.findViewById(R.id.tvCurrent)
        private val tvHigh: TextView = itemView.findViewById(R.id.tvHigh)
        private val tvDrawdown: TextView = itemView.findViewById(R.id.tvDrawdown)

        fun bind(item: StockEntity) {

            name.text = item.name
            tvCurrent.text = "%.2f".format(item.currentValue)
            tvHigh.text = "%.2f".format(item.allTimeHigh)
            tvDrawdown.text = String.format(Locale.US, "%.2f%%", item.drawdownPercent)

            val color =
                if (item.drawdownPercent < 0) Color.RED
                else Color.BLUE

            tvDrawdown.setTextColor(color)
        }
    }
}