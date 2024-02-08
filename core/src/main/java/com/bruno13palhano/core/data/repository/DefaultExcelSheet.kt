package com.bruno13palhano.core.data.repository

import android.content.Context
import com.bruno13palhano.core.data.di.DefaultWorkbook
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

internal class DefaultExcelSheet @Inject constructor(
    @DefaultWorkbook private val workbook: Workbook,
    @ApplicationContext private val context: Context
) : ExcelSheet {
    override suspend fun createExcel(
        sheetName: String,
        headers: List<String>,
        data: List<List<String>>
    ) {
        createWorkbook(sheetName = sheetName, headers = headers, data = data)
        val appDirectory = context.getExternalFilesDir("ShopdaniManagementSystem")

        if (appDirectory != null && !appDirectory.exists()) {
            appDirectory.mkdirs()
        }

        val excelFle = File(appDirectory, "${sheetName}.xlsx")
        try {
            val fileOut = FileOutputStream(excelFle)
            workbook.write(fileOut)
            fileOut.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createWorkbook(
        sheetName: String,
        headers: List<String>,
        data: List<List<String>>
    ): Workbook {
        val sheet: Sheet = if (workbook.getSheet(sheetName) == null) {
            workbook.createSheet(sheetName)
        } else {
            workbook.getSheet(sheetName)
        }

        val cellStyle = getHeaderStyle(workbook = workbook)
        createSheetHeader(headers = headers, cellStyle = cellStyle, sheet = sheet)

        for (i in data.indices) {
            addData(rowIndex = i+1, sheet = sheet, data = data)
        }

        return workbook
    }

    private fun getHeaderStyle(workbook: Workbook): CellStyle {
        val cellStyle: CellStyle = workbook.createCellStyle()
        val colorMap: IndexedColorMap = (workbook as XSSFWorkbook).stylesSource.indexedColors
        var color = XSSFColor(IndexedColors.RED, colorMap).indexed
        cellStyle.fillForegroundColor = color
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)

        val whiteFont = workbook.createFont()
        color = XSSFColor(IndexedColors.WHITE, colorMap).indexed
        whiteFont.color = color
        whiteFont.bold = true
        cellStyle.setFont(whiteFont)

        return cellStyle
    }

    private fun createSheetHeader(headers: List<String>, cellStyle: CellStyle, sheet: Sheet) {
        val row = sheet.createRow(0)

        for ((index, value) in headers.withIndex()) {
            val columnWith = (15 * 500)
            sheet.setColumnWidth(index, columnWith)
            val cell = row.createCell(index)
            cell?.setCellValue(value)
            cell.cellStyle = cellStyle
        }
    }

    private fun addData(rowIndex: Int, sheet: Sheet, data: List<List<String>>) {
        val row = sheet.createRow(rowIndex)

        for(i in data) {
            for ((indexColumn, value) in i.withIndex()) {
                createCell(row = row, columnIndex = indexColumn, value = value)
            }
        }
    }

    private fun createCell(row: Row, columnIndex: Int, value: String?) {
        val cell = row.createCell(columnIndex)
        cell?.setCellValue(value)
    }
}