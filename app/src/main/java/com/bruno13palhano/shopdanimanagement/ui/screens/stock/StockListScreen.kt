package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.components.StockItem
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
    var showCategoryDialog by remember { mutableStateOf(false) }

    StockListContent(
        categoryId = viewModel.name,
        showCategoryDialog = showCategoryDialog,
        stockList = stockList,
        onCategoryChange = viewModel::updateName,
        onOkClick = { viewModel.updateCategory(categoryId.toLong()) },
        onDismissRequest = { showCategoryDialog = false },
        onItemClick = onItemClick,
        onEditItemClick = {
            showCategoryDialog = true
        },
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListContent(
    categoryId: String,
    showCategoryDialog: Boolean,
    stockList: List<Stock>,
    onCategoryChange: (category: String) -> Unit,
    onOkClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onItemClick: (id: Long) -> Unit,
    onEditItemClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val menuOptions = arrayOf(
        stringResource(id = R.string.edit_label)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = categoryId) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
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
                                    items = menuOptions,
                                    expanded = expanded,
                                    onDismissRequest = { expanded = it },
                                    onClick = { index ->
                                        when (index) {
                                            0 -> {
                                                onEditItemClick()
                                            }
                                        }
                                    }
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(it)
        ) {
            items(stockList) { stock ->
                StockItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    photo = stock.photo,
                    name = stock.name,
                    price = stock.purchasePrice,
                    quantity = stock.quantity,
                    onClick = { onItemClick(stock.id) }
                )
            }
        }
        if (showCategoryDialog) {
            CategoryDialog(
                newCategory = categoryId,
                onCategoryChange = onCategoryChange,
                onOkClick = onOkClick,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun ProductListDynamicPreview() {
    val items = listOf(
        Stock(id= 1L, name = "Product 1", photo = "", purchasePrice = 120.45F, quantity = 12),
        Stock(id= 2L, name = "Product 2", photo = "", purchasePrice = 40.33F, quantity = 2),
        Stock(id= 3L, name = "Product 3", photo = "", purchasePrice = 99.99F, quantity = 7),
        Stock(id= 4L, name = "Product 4", photo = "", purchasePrice = 12.39F, quantity = 2),
        Stock(id= 5L, name = "Product 5", photo = "", purchasePrice = 56.78F, quantity = 1),
        Stock(id= 6L, name = "Product 6", photo = "", purchasePrice = 12.12F, quantity = 2),
    )

    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockListContent(
                showCategoryDialog = true,
                categoryId = "Perfume",
                stockList = items,
                onCategoryChange = {},
                onOkClick = {},
                onDismissRequest = {},
                onItemClick = {},
                onEditItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun ProductListPreview() {
    val items = listOf(
        Stock(id= 1L, name = "Product 1", photo = "", purchasePrice = 120.45F, quantity = 12),
        Stock(id= 2L, name = "Product 2", photo = "", purchasePrice = 40.33F, quantity = 2),
        Stock(id= 3L, name = "Product 3", photo = "", purchasePrice = 99.99F, quantity = 7),
        Stock(id= 4L, name = "Product 4", photo = "", purchasePrice = 12.39F, quantity = 2),
        Stock(id= 5L, name = "Product 5", photo = "", purchasePrice = 56.78F, quantity = 1),
        Stock(id= 6L, name = "Product 6", photo = "", purchasePrice = 12.12F, quantity = 2),
    )

    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockListContent(
                showCategoryDialog = true,
                categoryId = "Perfume",
                stockList = items,
                onCategoryChange = {},
                onOkClick = {},
                onDismissRequest = {},
                onItemClick = {},
                onEditItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}