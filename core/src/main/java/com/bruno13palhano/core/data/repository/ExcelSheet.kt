package com.bruno13palhano.core.data.repository

interface ExcelSheet {
    suspend fun createExcel(
        sheetName: String,
        headers: List<String>,
        data: List<List<String>>,
    )
}
