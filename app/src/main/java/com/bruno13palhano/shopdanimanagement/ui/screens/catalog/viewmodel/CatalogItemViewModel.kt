package com.bruno13palhano.shopdanimanagement.ui.screens.catalog.viewmodel

import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.CatalogData
import com.bruno13palhano.core.data.di.CatalogRep
import com.bruno13palhano.core.model.Catalog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CatalogItemViewModel @Inject constructor(
    @CatalogRep private val catalogRepository: CatalogData<Catalog>
) : ViewModel() {

}