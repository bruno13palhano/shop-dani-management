package com.bruno13palhano.shopdanimanagement.ui.screens.shopping

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CommonItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel.ShoppingViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun ShoppingScreen(
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: ShoppingViewModel = hiltViewModel()
) {
    val shoppingList by viewModel.shoppingList.collectAsStateWithLifecycle()

    ShoppingContent(
        itemList = shoppingList,
        onItemClick = onItemClick,
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingContent(
    itemList: List<CommonItem>,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.shopping_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
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
            items(itemList) { item ->
                CommonItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    subtitle = stringResource(id = R.string.quantity_tag, item.subtitle),
                    description = stringResource(id = R.string.date_of_shopping_tag, item.description),
                    onClick = { onItemClick(item.id) }
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
                navigateUp = {},
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
                navigateUp = {},
            )
        }
    }
}

private val shoppingList = listOf(
    CommonItem(id = 1L, photo = "", title = "Essencial", subtitle = "10", description = "Feb 4, 2023"),
    CommonItem(id = 2L, photo = "", title = "Homem", subtitle = "10", description = "Feb 7, 2023"),
    CommonItem(id = 3L, photo = "", title = "Luna", subtitle = "10", description = "Feb 9, 2023"),
    CommonItem(id = 4L, photo = "", title = "Una", subtitle = "10", description = "Feb 9, 2023"),
    CommonItem(id = 5L, photo = "", title = "Kaiak", subtitle = "10", description = "Feb 10, 2023"),
    CommonItem(id = 6L, photo = "", title = "Essencial", subtitle = "10", description = "Feb 12, 2023"),
    CommonItem(id = 7L, photo = "", title = "Homem", subtitle = "10", description = "Feb 12, 2023"),
    CommonItem(id = 8L, photo = "", title = "Kaiak", subtitle = "10", description = "Feb 14, 2023"),
    CommonItem(id = 10L, photo = "", title = "Essencial", subtitle = "10", description = "Feb 12, 2023"),
)