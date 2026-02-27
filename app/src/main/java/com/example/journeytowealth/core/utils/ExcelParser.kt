package com.example.journeytowealth.core.utils

import com.example.journeytowealth.data.local.ExcelData
import com.example.journeytowealth.data.local.index.MarketIndexDto
import com.example.journeytowealth.data.local.portfolio.PortfolioDto
import com.example.journeytowealth.data.local.stock.StockDto
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object  ExcelParser {

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
            when (type) {
                "INDEX" -> indexes.add(
                    MarketIndexDto(
                        name = row.getCell(1).stringCellValue,
                        ticker = row.getCell(2).stringCellValue,
                        currentValue = row.getCell(3).numericCellValue.toFloat(),
                        maxValue = row.getCell(4).numericCellValue.toFloat(),
                        percentBtwMaxAndCur = row.getCell(5).numericCellValue.toFloat()
                    )
                )

                "STOCK" -> stocks.add(
                    StockDto(
                        ticker = row.getCell(1).stringCellValue,
                        name = row.getCell(2).stringCellValue,
                        currentValue = row.getCell(3).numericCellValue.toFloat(),
                        maxValue = row.getCell(4).numericCellValue.toFloat(),
                        percentBtwMaxAndCur = row.getCell(5).numericCellValue.toFloat()
                    )
                )

                "PORTFOLIO" -> portfolios.add(
                    PortfolioDto(
                        name = row.getCell(2).stringCellValue,
                    )
                )
            }
        }

        workbook.close()
        return ExcelData(indexes, stocks, portfolios)
    }
}