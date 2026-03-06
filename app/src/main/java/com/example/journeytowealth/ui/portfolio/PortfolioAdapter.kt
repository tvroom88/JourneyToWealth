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
import com.example.journeytowealth.ui.state.PortfolioHeaderUiModel
import com.example.journeytowealth.ui.state.PortfolioItemUiModel
import com.example.journeytowealth.ui.state.PortfolioUi
import java.util.Locale

class PortfolioAdapter :
    ListAdapter<PortfolioUi, RecyclerView.ViewHolder>(DIFF) {

    companion object {
        private const val VIEW_TYPE_ACCOUNT = 0
        private const val VIEW_TYPE_HEADER = 1
        private const val VIEW_TYPE_ITEM = 2

        private val DIFF = object : DiffUtil.ItemCallback<PortfolioUi>() {
            override fun areItemsTheSame(oldItem: PortfolioUi, newItem: PortfolioUi): Boolean {
                return when {
                    oldItem is PortfolioUi.AccountType && newItem is PortfolioUi.AccountType -> oldItem.name == newItem.name
                    oldItem is PortfolioUi.Item && newItem is PortfolioUi.Item -> oldItem.data.etfName == newItem.data.etfName
                    else -> oldItem == newItem
                }
            }

            override fun areContentsTheSame(oldItem: PortfolioUi, newItem: PortfolioUi): Boolean =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is PortfolioUi.AccountType -> VIEW_TYPE_ACCOUNT
        is PortfolioUi.Header -> VIEW_TYPE_HEADER
        is PortfolioUi.Item -> VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_ACCOUNT -> AccountViewHolder(
                inflater.inflate(
                    R.layout.item_portfolio_account_title,
                    parent,
                    false
                )
            )

            VIEW_TYPE_HEADER -> HeaderViewHolder(
                inflater.inflate(
                    R.layout.item_portfolio_header,
                    parent,
                    false
                )
            )

            else -> PortfolioViewHolder(
                inflater.inflate(
                    R.layout.item_portfolio_row,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val uiModel = getItem(position)) {
            is PortfolioUi.AccountType -> (holder as AccountViewHolder).bind(uiModel)
            is PortfolioUi.Header -> (holder as HeaderViewHolder).bind(uiModel.data)
            is PortfolioUi.Item -> (holder as PortfolioViewHolder).bind(uiModel.data)
        }
    }

    // --- ViewHolder들 ---

    class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(model: PortfolioUi.AccountType) {
            itemView.findViewById<TextView>(R.id.tvAccountName).text = model.name
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(header: PortfolioHeaderUiModel) {
            // 헤더 데이터 바인딩 (예: 총액, 수익률 등)
//            itemView.findViewById<TextView>(R.id.tvAccountName).text = model.name

        }
    }

    class PortfolioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 기존에 작성하신 ETF 상세 정보 바인딩 로직 유지
        private val tvETF: TextView = itemView.findViewById(R.id.tvETF)
        private val tvCurrentWeight: TextView = itemView.findViewById(R.id.tvCurrentWeight)
        private val tvWeightDeviation: TextView = itemView.findViewById(R.id.tvWeightDeviation)
        private val tvTradeQuantity: TextView = itemView.findViewById(R.id.tvTradeQuantity)
        private val tvHoldingQuantity: TextView = itemView.findViewById(R.id.tvHoldingQuantity)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCurrentPrice)
        fun bind(item: PortfolioItemUiModel) {

            tvETF.text = item.etfName
            tvCurrentWeight.text =
                String.format(Locale.US, "%.2f%%", item.currentWeight)
            tvWeightDeviation.text =
                String.format(Locale.US, "%.2f%%", item.deviation)
            tvTradeQuantity.text = item.tradeQuantity.toString()
            tvHoldingQuantity.text = item.holdingQuantity.toString()
            tvCurrentPrice.text =
                String.format(Locale.US, "%,d", item.currentPrice)
            val color =
                if (item.deviation < 0) Color.RED
                else Color.BLUE

            tvWeightDeviation.setTextColor(color)
        }
    }
}

//class PortfolioViewHolder(itemView: View) :
//    RecyclerView.ViewHolder(itemView) {
//
//
//    private val tvETF: TextView = itemView.findViewById(R.id.tvETF)
//    private val tvCurrentWeight: TextView = itemView.findViewById(R.id.tvCurrentWeight)
//    private val tvWeightDeviation: TextView = itemView.findViewById(R.id.tvWeightDeviation)
//    private val tvTradeQuantity: TextView = itemView.findViewById(R.id.tvTradeQuantity)
//    private val tvHoldingQuantity: TextView = itemView.findViewById(R.id.tvHoldingQuantity)
//    private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCurrentPrice)
//
//    fun bind(item: PortfolioItemUiModel) {
//
//        tvETF.text = item.etfName
//
//        tvCurrentWeight.text =
//            String.format(Locale.US, "%.2f%%", item.currentWeight)
//
//        tvWeightDeviation.text =
//            String.format(Locale.US, "%.2f%%", item.deviation)
//
//        tvTradeQuantity.text = item.tradeQuantity.toString()
//
//        tvHoldingQuantity.text = item.holdingQuantity.toString()
//
//        tvCurrentPrice.text =
//            String.format(Locale.US, "%,d", item.currentPrice)
//
//        val color =
//            if (item.deviation < 0) Color.RED
//            else Color.BLUE
//
//        tvWeightDeviation.setTextColor(color)
//    }
//}