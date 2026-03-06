package com.example.journeytowealth.core.utils

import android.util.Log
import com.example.journeytowealth.data.model.ExcelData
import com.example.journeytowealth.data.remote.dto.MarketIndexDto
import com.example.journeytowealth.data.remote.dto.PortfolioDto
import com.example.journeytowealth.data.remote.dto.StockDto
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object ExcelParser {

    fun parse(bytes: ByteArray): ExcelData {

        val workbook = XSSFWorkbook(bytes.inputStream())
        val sheet = workbook.getSheetAt(0)

        val indexes = mutableListOf<MarketIndexDto>()
        val stocks = mutableListOf<StockDto>()
        val portfolios = mutableListOf<PortfolioDto>()

        for (i in 1..sheet.lastRowNum) {

            val row = sheet.getRow(i) ?: continue
            val type = getString(row.getCell(0))

            when (type) {

                "INDEX" -> {
                    indexes.add(
                        MarketIndexDto(
                            category = getString(row.getCell(0)),
                            name = getString(row.getCell(2)),
                            ticker = getString(row.getCell(4)),
                            currentValue = getFloat(row.getCell(5)),
                            allTimeHigh = getFloat(row.getCell(6)),
                            drawdownPercent = getFloat(row.getCell(7)) * 100
                        )
                    )
                }

                "STOCK" -> {
                    stocks.add(
                        StockDto(
                            category = getString(row.getCell(0)),
                            name = getString(row.getCell(2)),
                            ticker = getString(row.getCell(3)),
                            sector = getString(row.getCell(4)),
                            currentValue = getFloat(row.getCell(5)),
                            allTimeHigh = getFloat(row.getCell(6)),
                            drawdownPercent = getFloat(row.getCell(7)) * 100,
                            per = null,
                            eps = null
                        )
                    )
                }

                "PORTFOLIO" -> {
                    portfolios.add(
                        PortfolioDto(
                            category = getString(row.getCell(1)),
                            etfName = getString(row.getCell(2)),
                            targetWeight = getFloat(row.getCell(3)) * 100,
                            currentWeight = getFloat(row.getCell(4)) * 100,
                            deviation = getFloat(row.getCell(5)) * 100,
                            tradeQuantity = getInt(row.getCell(6)),
                            holdingQuantity = getInt(row.getCell(7)),
                            currentPrice = 100L,
                            evaluationAmount = 100
                        )
                    )
                }
            }
        }

        workbook.close()
        return ExcelData(indexes, stocks, portfolios)
    }

    private fun getString(cell: Cell?): String {

        if (cell == null) return ""

        return try {

            when (cell.cellType) {

                CellType.STRING ->
                    cell.stringCellValue.trim()

                CellType.NUMERIC ->
                    cell.numericCellValue.toString()

                CellType.BOOLEAN ->
                    cell.booleanCellValue.toString()

                CellType.FORMULA ->
                    when (cell.cachedFormulaResultType) {

                        CellType.STRING ->
                            cell.stringCellValue.trim()

                        CellType.NUMERIC ->
                            cell.numericCellValue.toString()

                        else -> ""
                    }

                else -> ""
            }

        } catch (e: Exception) {
            ""
        }
    }

    private fun getFloat(cell: Cell?): Float {

        if (cell == null) return 0f

        return try {

            when (cell.cellType) {

                CellType.NUMERIC ->
                    cell.numericCellValue.toFloat()

                CellType.STRING ->
                    cell.stringCellValue.trim().toFloatOrNull() ?: 0f

                CellType.FORMULA ->
                    when (cell.cachedFormulaResultType) {

                        CellType.NUMERIC ->
                            cell.numericCellValue.toFloat()

                        CellType.STRING ->
                            cell.stringCellValue.toFloatOrNull() ?: 0f

                        else -> 0f
                    }

                else -> 0f
            }

        } catch (e: Exception) {
            Log.d("ExcelParser", "Float parse error : ${e.message}")
            0f
        }
    }

    private fun getInt(cell: Cell?): Int {

        if (cell == null) return 0

        return try {

            when (cell.cellType) {

                CellType.NUMERIC ->
                    cell.numericCellValue.toInt()

                CellType.STRING ->
                    cell.stringCellValue.trim().toIntOrNull() ?: 0

                CellType.FORMULA ->
                    when (cell.cachedFormulaResultType) {

                        CellType.NUMERIC ->
                            cell.numericCellValue.toInt()

                        CellType.STRING ->
                            cell.stringCellValue.trim().toIntOrNull() ?: 0

                        else -> 0
                    }

                else -> 0
            }

        } catch (e: Exception) {
            Log.d("ExcelParser", "Int parse error : ${e.message}")
            0
        }
    }
}