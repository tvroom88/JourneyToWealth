package com.example.journeytowealth.ui.stock

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.journeytowealth.MyApplication
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.data.local.MarketIndexLocalDataSource
import com.example.journeytowealth.data.local.StockLocalDataSource
import com.example.journeytowealth.data.local.database.AppDatabase
import com.example.journeytowealth.data.remote.ExcelRemoteDataSource
import com.example.journeytowealth.data.repository.MainRepository
import com.example.journeytowealth.databinding.FragmentStockBinding

class StockFragment :
    BaseFragment<FragmentStockBinding>(FragmentStockBinding::inflate) {

    private val mainRepository by lazy { createRepository() }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // DB Flow 구독
        lifecycleScope.launchWhenStarted {
            mainRepository.observeDb().collect { dbData ->
                Log.d("DB_DATA", "Stocks: ${dbData.stocks.size}, Indexes: ${dbData.indexes.size}")


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
