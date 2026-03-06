package com.example.journeytowealth.ui.portfolio

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.journeytowealth.R
import com.example.journeytowealth.data.local.entity.PortfolioEntity
import java.util.Locale

class PortfolioAdapter :
    ListAdapter<PortfolioEntity, PortfolioAdapter.PortfolioViewHolder>(DIFF) {

    companion object {

        private val DIFF = object : DiffUtil.ItemCallback<PortfolioEntity>() {

            override fun areItemsTheSame(
                oldItem: PortfolioEntity,
                newItem: PortfolioEntity
            ): Boolean {
                return oldItem.etfName == newItem.etfName
            }

            override fun areContentsTheSame(
                oldItem: PortfolioEntity,
                newItem: PortfolioEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortfolioViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portfolio_row, parent, false)

        return PortfolioViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PortfolioViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class PortfolioViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvETF: TextView = itemView.findViewById(R.id.tvETF)
        private val tvCurrentWeight: TextView = itemView.findViewById(R.id.tvCurrentWeight)
        private val tvWeightDeviation: TextView = itemView.findViewById(R.id.tvWeightDeviation)
        private val tvTradeQuantity: TextView = itemView.findViewById(R.id.tvTradeQuantity)
        private val tvHoldingQuantity: TextView = itemView.findViewById(R.id.tvHoldingQuantity)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCurrentPrice)

        fun bind(item: PortfolioEntity) {
//            Log.d("PortfolioAdapter", "bind item = $item")

            tvETF.text = item.etfName

            tvCurrentWeight.text = String.format(Locale.US, "%.2f%%", item.currentWeight)
            tvWeightDeviation.text = String.format(Locale.US, "%.2f%%", item.deviation)
            tvTradeQuantity.text = item.tradeQuantity.toString()
            tvHoldingQuantity.text = item.holdingQuantity.toString()

            tvCurrentPrice.text = String.format(Locale.US, "%,d", item.currentPrice)
            val color =
                if (item.deviation < 0) Color.RED
                else Color.BLUE

            tvWeightDeviation.setTextColor(color)
        }
    }
}