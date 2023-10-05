package com.bruno13palhano.shopdanimanagement.ui.screens.common

data class ExtendedItem(
    val id: Long,
    val photo: ByteArray,
    val title: String,
    val firstSubtitle: String,
    val secondSubtitle: String,
    val description: String,
    val footer: String
)
