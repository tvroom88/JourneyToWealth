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

            Log.d("testtest", "type : $type")
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
                        percentBtwMaxAndCur = row.getCell(7).numericCellValue.toFloat() * 100,
                        per = null,
                        eps = null
                    )
                )

                "PORTFOLIO" -> Log.d("testtest", "type : $type")
//                    portfolios.add(
//                    PortfolioDto(
//                        name = row.getCell(2).stringCellValue,
//                    )
//                )
            }
        }

        workbook.close()
        return ExcelData(indexes, stocks, portfolios)
    }
}