package com.example.journeytowealth.core.utils

import android.util.Log
import com.example.journeytowealth.data.model.ExcelData
import com.example.journeytowealth.data.remote.dto.MarketIndexDto
import com.example.journeytowealth.data.remote.dto.PortfolioDto
import com.example.journeytowealth.data.remote.dto.StockDto
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object ExcelParser {

    fun parse(bytes: ByteArray): ExcelData {
        val inputStream = bytes.inputStream()
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)

        val indexes = mutableListOf<MarketIndexDto>()
        val stocks = mutableListOf<StockDto>()
        val portfolios = mutableListOf<PortfolioDto>()

        // 헤더 제외, 1행부터 데이터
        for (row in sheet.drop(1)) {
            val type = row.getCell(0).stringCellValue

//            if (type == "PORTFOLIO")
//                Log.d("CheckCheck", "${row.getCell(2)}")

            when (type) {
                "INDEX" -> indexes.add(
                    MarketIndexDto(
                        category = row.getCell(0).stringCellValue,
                        name = row.getCell(2).stringCellValue,
                        ticker = row.getCell(4).stringCellValue,
                        currentValue = row.getCell(5).numericCellValue.toFloat(),
                        allTimeHigh = row.getCell(6).numericCellValue.toFloat(),
                        drawdownPercent = row.getCell(7).numericCellValue.toFloat() * 100,
                    )
                )

                "STOCK" -> stocks.add(
                    StockDto(
                        category = row.getCell(0).stringCellValue,
                        name = row.getCell(2).stringCellValue,
                        ticker = row.getCell(3).stringCellValue,
                        sector = row.getCell(4).stringCellValue,
                        currentValue = row.getCell(5).numericCellValue.toFloat(),
                        allTimeHigh = row.getCell(6).numericCellValue.toFloat(),
                        drawdownPercent = row.getCell(7).numericCellValue.toFloat() * 100,
                        per = null,
                        eps = null
                    )
                )


//                "PORTFOLIO" -> portfolios.add(
//                    PortfolioDto(
//                        category = row.getCell(1).stringCellValue,
//                        etfName = row.getCell(2).stringCellValue,
//                        targetWeight = row.getCell(3).stringCellValue.toFloat(),
//                        currentWeight = row.getCell(4).stringCellValue.toFloat(),
//                        deviation = row.getCell(5).stringCellValue.toFloat(),
//                        tradeQuantity = 1,
//                        holdingQuantity = 2,
//                        currentPrice = 100L,
//                        evaluationAmount = 100
//                    )
//                )
            }
        }

//        val category: String,          // 표시 (ETF 이름)
//        val etfName: String,        // ETF 코드
//        val targetWeight: Float,   // 목표 비중 (%)
//        val currentWeight: Float,  // 실제 비중 (%)
//        val deviation: Float,      // 이격율 (%)
//        val tradeQuantity: Int,    // 매매 수량
//        val holdingQuantity: Int,  // 보유 수량
//        val currentPrice: Long,    // 현재가
//        val evaluationAmount: Long // 평가액

        workbook.close()
        return ExcelData(indexes, stocks, portfolios)
    }
}