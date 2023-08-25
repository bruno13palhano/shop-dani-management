package com.bruno13palhano.shopdanimanagement.ui.screens.shopping

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CommonPhotoItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel.ShoppingProductListViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun ShoppingProductListScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: ShoppingProductListViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllProducts()
    }

    val menuOptions = mutableListOf(stringResource(id = R.string.all_products_label))
    val productList by viewModel.productList.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    menuOptions.addAll(categories)

    ShoppingProductListContent(
        itemList = productList,
        itemsMenu = menuOptions.toTypedArray(),
        onItemClick = onItemClick,
        onItemMenuClick = { index ->
            if (index == 0) {
                viewModel.getAllProducts()
            } else {
                viewModel.getProductByCategory(menuOptions[index])
            }
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingProductListContent(
    itemList: List<CommonItem>,
    itemsMenu: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onItemMenuClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.products_label)) },
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
                                    items = itemsMenu,
                                    expanded = expanded,
                                    onDismissRequest = { expandedValue ->
                                        expanded = expandedValue
                                    },
                                    onClick = onItemMenuClick
                                )
                            }
                        }
                    }
                }
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(items = itemList, key = { item -> item.id }) { item ->
                CommonPhotoItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    photo = item.photo,
                    title = item.title,
                    subtitle = item.subtitle,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ShoppingProductListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingProductListContent(
                itemList = productList,
                itemsMenu = emptyArray(),
                onItemClick = {},
                onItemMenuClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ShoppingProductListPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingProductListContent(
                itemList = productList,
                itemsMenu = emptyArray(),
                onItemClick = {},
                onItemMenuClick = {},
                navigateUp = {}
            )
        }
    }
}

private val productList = listOf(
    CommonItem(id= 1L, photo = "", title = "Kaiak", subtitle = "Natura", description = ""),
    CommonItem(id= 2L, photo = "", title = "Essencial", subtitle = "Natura", description = ""),
    CommonItem(id= 3L, photo = "", title = "Essential", subtitle = "Avon", description = ""),
    CommonItem(id= 4L, photo = "", title = "Florata", subtitle = "Avon", description = ""),
    CommonItem(id= 5L, photo = "", title = "Homem", subtitle = "Natura", description = ""),
    CommonItem(id= 6L, photo = "", title = "Luna", subtitle = "Natura", description = ""),
    CommonItem(id= 7L, photo = "", title = "Florata", subtitle = "Avon", description = ""),
    CommonItem(id= 8L, photo = "", title = "Homem", subtitle = "Natura", description = ""),
)