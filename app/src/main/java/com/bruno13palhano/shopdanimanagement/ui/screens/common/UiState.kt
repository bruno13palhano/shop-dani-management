package com.bruno13palhano.shopdanimanagement.ui.screens.common

sealed class UiState {
    data object Success: UiState()
    data object InProgress: UiState()
    data object Fail: UiState()
}