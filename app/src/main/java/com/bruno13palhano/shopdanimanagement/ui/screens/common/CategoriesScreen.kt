package com.bruno13palhano.shopdanimanagement.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.CategoriesContent
import com.bruno13palhano.shopdanimanagement.ui.screens.common.viewmodel.CategoriesViewModel

@Composable
fun CategoriesScreen(
    onItemClick: (categoryId: String) -> Unit,
    navigateUp: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var showCategoryDialog by remember { mutableStateOf(false) }

    CategoriesContent(
        newCategory = viewModel.newName,
        showCategoryDialog = showCategoryDialog,
        categories = categories,
        onAddButtonClick = { showCategoryDialog = true },
        onCategoryChange = viewModel::updateName,
        onDismissRequest = { showCategoryDialog = false },
        onOkClick = { viewModel.insertCategory() },
        onItemClick = onItemClick,
        navigateBack = navigateUp
    )
}