package com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.di.DefaultShoppingRepository
import com.bruno13palhano.core.model.Shopping
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    @DefaultShoppingRepository private val shoppingRepository: ShoppingData<Shopping>
) : ViewModel() {
    val shoppingList = shoppingRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}