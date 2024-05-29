package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesContent(
    snackbarHostState: SnackbarHostState,
    newCategory: String,
    showCategoryDialog: Boolean,
    categories: List<Category>,
    onAddButtonClick: () -> Unit,
    onCategoryChange: (newCategory: String) -> Unit,
    onDismissRequest: () -> Unit,
    onOkClick: () -> Unit,
    onItemClick: (categoryId: Long) -> Unit,
    onIconMenuClick: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.product_categories_label)) },
                navigationIcon = {
                    IconButton(onClick = onIconMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        }
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .semantics { contentDescription = "List of items" }
                    .padding(it),
            contentPadding = PaddingValues(8.dp, vertical = 4.dp)
        ) {
            items(categories) { category ->
                SimpleItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    itemName = category.category,
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward
                ) {
                    onItemClick(category.id)
                }
            }
        }
    }
    if (showCategoryDialog) {
        SingleInputDialog(
            dialogTitle = stringResource(id = R.string.new_category_label),
            label = stringResource(id = R.string.name_label),
            placeholder = stringResource(id = R.string.enter_name_label),
            input = newCategory,
            onInputChange = onCategoryChange,
            onOkClick = onOkClick,
            onDismissRequest = onDismissRequest
        )
    }
}