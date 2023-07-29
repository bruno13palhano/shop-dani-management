package com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.DataOperations
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    @DefaultCategoryRepository private val categoryRepository: DataOperations<Category>
): ViewModel() {
    var name by mutableStateOf("")
        private set

    fun updateName(name: String) {
        this.name = name
    }

    fun updateCategory(id: Long) {
        val category = Category(id, name)
        viewModelScope.launch {
            categoryRepository.update(category)
        }
    }

    fun getCategory(id: Long) {
        viewModelScope.launch {
            categoryRepository.getById(id).collect {
                name = it.name
            }
        }
    }
}