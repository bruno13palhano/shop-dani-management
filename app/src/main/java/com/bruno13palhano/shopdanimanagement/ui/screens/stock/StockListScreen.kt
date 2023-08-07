package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.ProductListContent
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel.StockListViewModel

@Composable
fun StockListScreen(
    categoryId: Long,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: StockListViewModel = hiltViewModel()
) {
    val stockList by viewModel.stock.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getCategory(categoryId)
    }
    LaunchedEffect(key1 = viewModel.name) {
        viewModel.getProductsByCategory(viewModel.name)
    }

    var showCategoryDialog by remember { mutableStateOf(false) }

    ProductListContent(
        categoryId = viewModel.name.ifEmpty {
            stringResource(id = R.string.all_products_label)
        },
        showCategoryDialog = showCategoryDialog,
        itemList = stockList,
        onCategoryChange = viewModel::updateName,
        onOkClick = { viewModel.updateCategory(categoryId) },
        onDismissRequest = { showCategoryDialog = false },
        onItemClick = onItemClick,
        onEditItemClick = {
            showCategoryDialog = true
        },
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}