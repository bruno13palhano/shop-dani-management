package com.bruno13palhano.shopdanimanagement.ui.screens.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
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
import com.bruno13palhano.shopdanimanagement.ui.components.PhotoItem
import com.bruno13palhano.shopdanimanagement.ui.screens.common.viewmodel.ProductItemListViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun ProductItemListScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: ProductItemListViewModel = hiltViewModel()
) {
    val productList by viewModel.productList.collectAsStateWithLifecycle()

    ProductItemListContent(
        productList = productList,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItemListContent(
    productList: List<CommonItem>,
    onItemClick: (id: Long) -> Unit,
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
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(productList) { item ->
                PhotoItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    subtitle = item.subtitle,
                    photo = item.photo,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ProductItemListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductItemListContent(
                productList = productList,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ProductItemListPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductItemListContent(
                productList = productList,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val productList = listOf(
    CommonItem(id = 1L, title = "Essencial", photo = "", subtitle = "Natura"),
    CommonItem(id = 2L, title = "Homem", photo = "", subtitle = "Natura"),
    CommonItem(id = 3L, title = "Una", photo = "", subtitle = "Natura"),
    CommonItem(id = 4L, title = "Kaiak", photo = "", subtitle = "Natura"),
    CommonItem(id = 5L, title = "Luna", photo = "", subtitle = "Natura"),
    CommonItem(id = 6L, title = "Essencial", photo = "", subtitle = "Natura"),
    CommonItem(id = 7L, title = "Homem", photo = "", subtitle = "Natura"),
    CommonItem(id = 8L, title = "Una", photo = "", subtitle = "Natura"),
    CommonItem(id = 9L, title = "Kaiak", photo = "", subtitle = "Natura"),
    CommonItem(id = 10L, title = "Luna", photo = "", subtitle = "Natura")
)