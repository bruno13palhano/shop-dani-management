package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularItemList
import com.bruno13palhano.shopdanimanagement.ui.components.CommonItemList
import com.bruno13palhano.shopdanimanagement.ui.components.CommonPhotoItemList
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleItemList
import com.bruno13palhano.shopdanimanagement.ui.components.StockItem
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true)
@Composable
private fun StockItemPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockItem(
                modifier = Modifier
                    .fillMaxSize(),
                name = "Essencial",
                photo = byteArrayOf(),
                price = 178.99f,
                quantity = 10,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StockListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SimpleItemList(
                modifier = Modifier.fillMaxWidth(),
                itemName = "Perfumes",
                imageVector = Icons.Filled.ArrowForward,
                onClick = {}
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun SaleItemListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CommonItemList(
                title = "Bruno",
                subtitle = "Essencial",
                description = "Feb 19, 2003",
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoItemPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CommonPhotoItemList(
                title = "Bruno",
                subtitle = "Rua 15 de novembro",
                photo = byteArrayOf(),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalItemListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HorizontalItemList(
                title = "Bruno",
                subtitle = "Rua 15 de novembro",
                description = "Test",
                photo = byteArrayOf(),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CircularItemListPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CircularItemList(
                title = stringResource(id = R.string.sales_label),
                icon = Icons.Filled.Image,
                onClick = {}
            )
        }
    }
}