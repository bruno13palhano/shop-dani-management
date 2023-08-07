package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.CategoriesContent
import com.bruno13palhano.shopdanimanagement.ui.components.CategoriesItems
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel.StockCategoriesViewModel

@Composable
fun StockCategoriesScreen(
    onItemClick: (categoryId: String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: StockCategoriesViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    var showCategoryDialog by remember { mutableStateOf(false) }

    CategoriesContent(
        newCategory = viewModel.newName,
        showCategoryDialog = showCategoryDialog,
        categories = categories,
        onAddButtonClick = {
            showCategoryDialog = true
        },
        onCategoryChange = viewModel::updateName,
        onOkClick = {
            viewModel.insertCategory()
        },
        onDismissRequest = {
            showCategoryDialog = false
        },
        onItemClick = onItemClick,
        onMoreOptionsItemClick = { index ->
            when (index) {
                CategoriesItems.allProducts -> {
                    onItemClick(index.toString())
                }
            }
        },
        navigateBack = navigateBack,
    )
}