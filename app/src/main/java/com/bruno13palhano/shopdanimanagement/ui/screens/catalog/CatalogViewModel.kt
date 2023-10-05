package com.bruno13palhano.shopdanimanagement.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.di.CatalogRep
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ExtendedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    @CatalogRep private val catalogRepository: CatalogData<Catalog>
) : ViewModel() {
    private val _catalogItems = MutableStateFlow(emptyList<Catalog>())
    val catalogItems = _catalogItems
        .map {
            it.map { item ->
                ExtendedItem(
                    id = item.id,
                    photo = item.photo,
                    title = item.title,
                    firstSubtitle = item.name,
                    secondSubtitle = item.price.toString(),
                    description = item.description,
                    footer = item.discount.toString()
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getAll() {
        viewModelScope.launch {
            catalogRepository.getAll().collect {
                _catalogItems.value = it
            }
        }
    }

    fun getOrderedByName(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            catalogRepository.getOrderedByName(isOrderedAsc = isOrderedAsc).collect {
                _catalogItems.value = it
            }
        }
    }

    fun getOrderedByPrice(isOrderedAsc: Boolean) {
        viewModelScope.launch {
            catalogRepository.getOrderedByPrice(isOrderedAsc = isOrderedAsc).collect {
                _catalogItems.value = it
            }
        }
    }
}