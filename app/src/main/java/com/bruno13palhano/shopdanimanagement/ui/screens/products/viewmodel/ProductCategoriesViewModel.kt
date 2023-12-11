package com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCategoriesViewModel @Inject constructor(
    @CategoryRep private val categoryRepository: CategoryRepository
): ViewModel() {
    val categories = categoryRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var newName by mutableStateOf("")
        private set

    fun updateName(newName: String) {
        this.newName = newName
    }

    fun insertCategory() {
        val category = Category(
            id = 0L,
            category = newName.trim(),
            timestamp = getCurrentTimestamp()
        )
        viewModelScope.launch {
            categoryRepository.insert(
                model = category,
                onError = {},
                onSuccess = {}
            )
        }
        restoreValue()
    }

    private fun restoreValue() {
        newName = ""
    }
}