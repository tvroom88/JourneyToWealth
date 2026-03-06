package com.example.journeytowealth.ui.stock

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journeytowealth.MyApplication
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.ui.state.UiState
import com.example.journeytowealth.data.local.MarketIndexLocalDataSource
import com.example.journeytowealth.data.local.StockLocalDataSource
import com.example.journeytowealth.data.local.database.AppDatabase
import com.example.journeytowealth.data.local.entity.MarketIndexEntity
import com.example.journeytowealth.data.remote.ExcelRemoteDataSource
import com.example.journeytowealth.data.repository.MainRepository
import com.example.journeytowealth.databinding.FragmentStockBinding
import com.example.journeytowealth.ui.main.MainViewModel

class StockFragment :
    BaseFragment<FragmentStockBinding>(FragmentStockBinding::inflate) {

    private val mainRepository by lazy { createRepository() }

    private val marketIndexAdapter by lazy { MarketIndexAdapter() }
    private val stockAdapter by lazy { StockAdapter() }

    // Activity ViewModel (Loading창 보여주고 없애주는 부분)
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerIndex.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerIndex.adapter = marketIndexAdapter

        binding.recyclerStock.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerStock.adapter = stockAdapter

        binding.btnToggle.setOnClickListener {
            val showIndex = binding.indexContainer.visibility == View.VISIBLE

            if (showIndex) {
                binding.indexContainer.visibility = View.GONE
                binding.stockContainer.visibility = View.VISIBLE
                binding.btnToggle.text = "주식"
                binding.tvTitle.text = "주식정보"
            } else {
                binding.indexContainer.visibility = View.VISIBLE
                binding.stockContainer.visibility = View.GONE
                binding.btnToggle.text = "지수"
                binding.tvTitle.text = "지수정보"
            }
        }

        // DB에 저장된 내용 구독
        lifecycleScope.launchWhenStarted {
            mainRepository.observeDb().collect { state ->

                when (state) {
                    is UiState.Loading -> {
                        // 로딩 표시
                        Log.d("DB_DATA", "Loading...")
                        mainViewModel.showLoading("DB에서 데이터를 가져오는 중입니다.")
                    }

                    is UiState.Success -> {
                        val dbData = state.data

                        Log.d(
                            "DB_DATA",
                            "Stocks: ${dbData.stocks.size}, Indexes: ${dbData.indexes.size}"
                        )

                        marketIndexAdapter.submitList(dbData.indexes)
                        stockAdapter.submitList(dbData.stocks)
                        mainViewModel.hideLoading()
                    }

                    is UiState.Error -> {
                        Log.e("DB_DATA", "Error: ${state.message}")
                        mainViewModel.hideLoading()
                    }
                }
            }
        }

    }

    private fun createRepository(): MainRepository {

        val db = AppDatabase.getInstance(MyApplication.appContext)
        return MainRepository(
            excelRemoteDataSource = ExcelRemoteDataSource(),
            stockLocalDataSource = StockLocalDataSource(db.stockDao()),
            marketIndexLocalDataSource =
                MarketIndexLocalDataSource(db.marketIndexDao())
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = StockFragment()
    }
}
