package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleItemList
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel.StockCategoriesViewModel

@Composable
fun StockCategoriesScreen(
    onItemClick: (categoryId: String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: StockCategoriesViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    var showCategoryDialog by remember { mutableStateOf(false) }

    StockCategoriesContent(
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
                StockCategoriesItems.allProducts -> {
                    onItemClick(index.toString())
                }
            }
        },
        navigateBack = navigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockCategoriesContent(
    newCategory: String,
    showCategoryDialog: Boolean,
    categories: List<Category>,
    onAddButtonClick: () -> Unit,
    onCategoryChange: (newCategory: String) -> Unit,
    onDismissRequest: () -> Unit,
    onOkClick: () -> Unit,
    onItemClick: (categoryId: String) -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    navigateBack: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val items = arrayOf(
        stringResource(id = R.string.all_products_label)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.stock_categories_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.more_options_label)
                            )
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MoreOptionsMenu(
                                    items = items,
                                    expanded = expanded,
                                    onDismissRequest = { expandedValue -> expanded = expandedValue},
                                    onClick = onMoreOptionsItemClick
                                )
                            }
                        }
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
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(categories) { category ->
                SimpleItemList(itemName = category.name, imageVector = Icons.Filled.ArrowForward) {
                    onItemClick(category.id.toString())
                }
            }
        }
    }
    if(showCategoryDialog) {
        CategoryDialog(
            newCategory = newCategory,
            onCategoryChange = onCategoryChange,
            onOkClick = onOkClick,
            onDismissRequest = onDismissRequest
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StockCategoriesListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val categories = listOf(
                Category(1L, "Gifts"),
                Category(2L, "Infant"),
                Category(3L, "Perfumes"),
                Category(4L, "Soaps"),
                Category(5L, "Antiperspirant Deodorants"),
                Category(6L, "Deodorants Cologne"),
                Category(7L, "Sunscreens"),
                Category(8L, "Makeup"),
                Category(9L, "Face"),
                Category(10L, "Skin"),
                Category(11L, "Hair"),
                Category(12L, "Moisturizers")
            )

            StockCategoriesContent(
                newCategory = "",
                showCategoryDialog = true,
                categories = categories,
                onAddButtonClick = {},
                onCategoryChange = {},
                onDismissRequest = {},
                onOkClick = {},
                onItemClick = {},
                onMoreOptionsItemClick = {},
                navigateBack = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    newCategory: String,
    onCategoryChange: (newCategory: String) -> Unit,
    onOkClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(10)),
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.new_category_label),
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    value = newCategory,
                    onValueChange = onCategoryChange,
                    label = { Text(text = stringResource(id = R.string.name_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.enter_name_label)) }
                )
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Button(
                        modifier = Modifier.padding(end = 4.dp),
                        onClick = {
                            onDismissRequest()
                            onOkClick()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.ok_label))
                    }
                    Button(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = onDismissRequest
                    ) {
                        Text(text = stringResource(id = R.string.cancel_label))
                    }
                }
            }
        }
    }
}

object StockCategoriesItems {
    const val allProducts = 0
}