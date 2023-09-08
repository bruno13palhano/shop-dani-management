package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.shopping.ShoppingContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

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