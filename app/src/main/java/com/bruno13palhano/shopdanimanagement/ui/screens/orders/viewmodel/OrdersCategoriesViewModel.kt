package com.bruno13palhano.shopdanimanagement.ui.screens.orders.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersCategoriesViewModel @Inject constructor(
    @DefaultCategoryRepository private val categoryRepository: CategoryData<Category>
): ViewModel() {
    var categories = categoryRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var newName by mutableStateOf("")
        private set

    fun updateName(newName: String) {
        this.newName = newName
    }

    fun insertCategory() {
        val category = Category(0L, newName)
        viewModelScope.launch {
            categoryRepository.insert(category)
        }
        restoreValue()
    }

    private fun restoreValue() {
        newName = ""
    }
}