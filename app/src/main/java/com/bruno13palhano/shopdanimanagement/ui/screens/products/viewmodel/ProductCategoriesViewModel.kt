package com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.data.di.SecondaryCategoryRepository
import com.bruno13palhano.core.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCategoriesViewModel @Inject constructor(
    @SecondaryCategoryRepository private val categoryRepository: CategoryData<Category>
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
        val category = Category(0L, newName.trim())
        viewModelScope.launch {
            categoryRepository.insert(category)
        }
        restoreValue()
    }

    private fun restoreValue() {
        newName = ""
    }
}