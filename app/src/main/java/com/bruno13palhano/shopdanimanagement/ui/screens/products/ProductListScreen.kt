package com.bruno13palhano.shopdanimanagement.ui.screens.products

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
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.ProductListViewModel

@Composable
fun ProductListScreen(
    isEditable: Boolean,
    categoryId: Long,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    if (isEditable) {
        LaunchedEffect(key1 = Unit) {
            viewModel.getCategory(categoryId)
        }
        LaunchedEffect(key1 = viewModel.name) {
            viewModel.getProductsByCategory(viewModel.name)
        }
    } else {
        LaunchedEffect(key1 = Unit) {
            viewModel.getAllProducts()
        }
    }

    val menuOptions = mutableListOf(stringResource(id = R.string.all_products_label))
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val orderList by viewModel.orders.collectAsStateWithLifecycle()
    var showCategoryDialog by remember { mutableStateOf(false) }
    menuOptions.addAll(categories)

    ProductListContent(
        isEditable = isEditable,
        categoryId = viewModel.name,
        showCategoryDialog = showCategoryDialog,
        itemList = orderList,
        menuOptions = menuOptions.toTypedArray(),
        onCategoryChange = viewModel::updateName,
        onOkClick = { viewModel.updateCategory(categoryId) },
        onDismissRequest = { showCategoryDialog = false },
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onEditItemClick = { showCategoryDialog = true },
        onMenuItemClick = { index ->
            if (index == 0) {
                viewModel.getAllProducts()
            } else {
                viewModel.getProductsByCategory(menuOptions[index])
            }
        },
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}