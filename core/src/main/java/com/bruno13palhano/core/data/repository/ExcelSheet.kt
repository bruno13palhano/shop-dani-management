package com.bruno13palhano.core.data.repository

interface ExcelSheet {
    fun createExcel(
        sheetName: String,
        headers: List<String>,
        data: List<List<String>>
    )
}