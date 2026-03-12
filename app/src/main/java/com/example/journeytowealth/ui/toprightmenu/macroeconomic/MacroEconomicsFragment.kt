package com.example.journeytowealth.ui.toprightmenu.macroeconomic

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.journeytowealth.R
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.core.constant.Constants
import com.example.journeytowealth.core.utils.FileDownloadManager
import com.example.journeytowealth.data.repository.MacroRepository
import com.example.journeytowealth.databinding.FragmentMacroEconomicsBinding

/**
 * FedWatch
 */
class MacroEconomicsFragment :
    BaseFragment<FragmentMacroEconomicsBinding>(FragmentMacroEconomicsBinding::inflate) {

    val repository by lazy { MacroRepository(mContext) }
    val factory by lazy { MacroViewModelFactory(repository) }
    private lateinit var viewModel: MacroViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            factory
        )[MacroViewModel::class.java] // 1. ViewModel 초기화 (위에서 만든 Factory 사용)
        setTopUi()
        setupObservers() // 2. Observer 연결 (데이터가 들어오면 UI를 바꿈)
        viewModel.loadMacroData() // 3. ★ 핵심: 화면이 시작되자마자 데이터 로딩 함수 호출


        //----------
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        ContextCompat.registerReceiver(
            requireContext(),
            downloadReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED // 시스템 브로드캐스트(DownloadManager)를 받아야 하므로 EXPORTED
        )
    }


    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L) {
                // ★ 다운로드가 끝났으니 이제 데이터를 읽으라고 ViewModel에 알림
                viewModel.loadMacroData()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 다운로드 완료 이벤트를 수신할 리시버 등록
//        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
//        requireContext().registerReceiver(downloadReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(downloadReceiver)
    }

    fun setTopUi() {
        binding.layoutGdp.ivFresh.setOnClickListener {
            viewModel.downloadUsaGdpCsvFile()
        }
    }

    private fun setupObservers() {
        // 메인 성장률 (예: 4.1%)
        viewModel.gdpYoY.observe(viewLifecycleOwner) { growth ->
            binding.layoutGdp.tvGdpMacroTitlePercentage.text = growth


        }

        // 하단 보조 정보 (예: +YoY : 0.5%)
        viewModel.gdpMom.observe(viewLifecycleOwner) { mom ->
            binding.layoutGdp.tvGdpMacroMom.text = "MoM : $mom"

            // 플러스면 빨간색(또는 초록색), 마이너스면 파란색으로 색상 변경 팁
            var stockColor = Color.RED
            var stockImage = R.drawable.ic_stock_up
            if (mom.contains("+")) {
                stockColor = Color.BLUE
                stockImage = R.drawable.ic_stock_up
            } else {
                stockColor = Color.RED
                stockImage = R.drawable.ic_stock_down
            }
//            val color = if (mom.contains("+")) Color.RED else Color.BLUE
            binding.layoutGdp.tvGdpMacroMom.setTextColor(stockColor)
            binding.layoutGdp.ivTrendIcon.setImageResource(stockImage)
        }
    }

    fun readGdpData() {
        val fromWhichYear = 2020
        try {
            // 예: 2022년 이후의 GDP 데이터만 가져오기
            val gdpData =
                FileDownloadManager.readCsvFile(
                    mContext,
                    Constants.FileName.US_GDP_FILE
                ) { date, _ ->
                    val year = date.take(4).toIntOrNull() ?: 0
                    year >= fromWhichYear
                }

            gdpData.forEach { (date, value) ->
                Log.d("GDP_FILTER", "날짜: $date, 수치: $value")
            }

            if (gdpData.isNotEmpty()) {
//                binding.textView.text = "${gdpData.size}개의 데이터를 불러왔습니다."
            }
        } catch (e: Exception) {
            Toast.makeText(mContext, "e : ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }
}
