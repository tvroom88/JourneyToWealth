package com.example.journeytowealth.core.base


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.journeytowealth.MyApplication
import com.example.journeytowealth.R
import com.example.journeytowealth.data.local.MarketIndexLocalDataSource
import com.example.journeytowealth.data.local.PortfolioLocalDataSource
import com.example.journeytowealth.data.local.StockLocalDataSource
import com.example.journeytowealth.data.local.database.AppDatabase
import com.example.journeytowealth.data.remote.ExcelRemoteDataSource
import com.example.journeytowealth.data.repository.MainRepository

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("ViewBinding is only valid between onCreateView and onDestroyView")


    lateinit var mContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)

        binding.root.setBackgroundColor(
            requireContext().getColor(R.color.bg_main)
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun createRepository(): MainRepository {
        val db = AppDatabase.getInstance(MyApplication.appContext)
        return MainRepository(
            excelRemoteDataSource = ExcelRemoteDataSource(),
            stockLocalDataSource = StockLocalDataSource(db.stockDao()),
            marketIndexLocalDataSource =
                MarketIndexLocalDataSource(db.marketIndexDao()),
            portfolioLocalDataSource = PortfolioLocalDataSource(db.portfolioDao())
        )
    }
}