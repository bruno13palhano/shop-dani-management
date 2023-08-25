package com.bruno13palhano.shopdanimanagement.ui.screens.sales

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SaleProductsViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun SaleProductsScreen(
    onItemClick: (productId: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: SaleProductsViewModel = hiltViewModel()
) {
    val productList by viewModel.products.collectAsStateWithLifecycle()

    SaleProductContent(
        productList = productList,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleProductContent(
    productList: List<CommonItem>,
    onItemClick: (productId: Long) -> Unit,
    navigateUp: () -> Unit
) {
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
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            items(items = productList, key = { product -> product.id }) { item ->
                HorizontalItemList(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    photo = item.photo,
                    title = item.title,
                    subtitle = item.subtitle,
                    description = item.description,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SaleProductsDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SaleProductContent(
                productList = itemList,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SaleProductsPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SaleProductContent(
                productList = itemList,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val itemList = listOf(
    CommonItem(id= 1L, photo = "", title = "Kaiak", subtitle = "Natura", description = "Feb 3, 2023"),
    CommonItem(id= 2L, photo = "", title = "Essencial", subtitle = "Natura", description = "Feb 5, 2023"),
    CommonItem(id= 3L, photo = "", title = "Essential", subtitle = "Avon", description = "Feb 7, 2023"),
    CommonItem(id= 4L, photo = "", title = "Florata", subtitle = "Avon", description = "Feb 7, 2023"),
    CommonItem(id= 5L, photo = "", title = "Homem", subtitle = "Natura", description = "Feb 12, 2023"),
    CommonItem(id= 6L, photo = "", title = "Luna", subtitle = "Natura", description = "Feb 19, 2023"),
    CommonItem(id= 7L, photo = "", title = "Florata", subtitle = "Avon", description = "Feb 20, 2023"),
    CommonItem(id= 8L, photo = "", title = "Homem", subtitle = "Natura", description = "Feb 23, 2023"),
)