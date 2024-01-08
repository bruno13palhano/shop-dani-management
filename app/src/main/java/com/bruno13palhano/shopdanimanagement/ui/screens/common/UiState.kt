package com.bruno13palhano.shopdanimanagement.ui.screens.common

sealed class UiState {
    object Success: UiState()
    object InProgress: UiState()
    object Fail: UiState()
}