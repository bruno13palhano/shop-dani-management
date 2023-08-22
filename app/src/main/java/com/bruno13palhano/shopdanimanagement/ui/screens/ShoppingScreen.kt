package com.bruno13palhano.shopdanimanagement.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.Shopping
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CommonItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel.ShoppingViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun ShoppingScreen(
    onItemClick: (id: Long) -> Unit,
    onMenuClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    viewModel: ShoppingViewModel = hiltViewModel()
) {
    val shoppingList by viewModel.shoppingList.collectAsStateWithLifecycle()

    ShoppingContent(
        itemList = shoppingList,
        onItemClick = onItemClick,
        onAddButtonClick = onAddButtonClick,
        onMenuClick = onMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingContent(
    itemList: List<Shopping>,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.shopping_label)) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
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
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(itemList) { shoppingItem ->
                CommonItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = shoppingItem.name,
                    subtitle = shoppingItem.quantity.toString(),
                    description = dateFormat.format(shoppingItem.date),
                    onClick = { onItemClick(shoppingItem.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ShoppingDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingContent(
                itemList = shoppingList,
                onItemClick = {},
                onAddButtonClick = {},
                onMenuClick = {},
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ShoppingPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingContent(
                itemList = shoppingList,
                onItemClick = {},
                onAddButtonClick = {},
                onMenuClick = {},
            )
        }
    }
}

private val shoppingList = listOf(
    Shopping(id = 1L, productId = 1L, name = "Essencial", purchasePrice = 123.50F, quantity = 10, date = 0L, isPaid = false),
    Shopping(id = 1L, productId = 1L, name = "Homem", purchasePrice = 123.50F, quantity = 10, date = 0L, isPaid = false),
    Shopping(id = 1L, productId = 1L, name = "Luna", purchasePrice = 200.90F, quantity = 10, date = 0L, isPaid = true),
    Shopping(id = 1L, productId = 1L, name = "Una", purchasePrice = 67.90F, quantity = 10, date = 0L, isPaid = true),
    Shopping(id = 1L, productId = 1L, name = "Kaiak", purchasePrice = 88.99F, quantity = 10, date = 0L, isPaid = false),
    Shopping(id = 1L, productId = 1L, name = "Essencial", purchasePrice = 123.50F, quantity = 10, date = 0L, isPaid = true),
    Shopping(id = 1L, productId = 1L, name = "Homem", purchasePrice = 88.99F, quantity = 10, date = 0L, isPaid = true),
    Shopping(id = 1L, productId = 1L, name = "Kaiak", purchasePrice = 113.90F, quantity = 10, date = 0L, isPaid = false),
    Shopping(id = 1L, productId = 1L, name = "Essencial", purchasePrice = 163.50F, quantity = 10, date = 0L, isPaid = false),
)