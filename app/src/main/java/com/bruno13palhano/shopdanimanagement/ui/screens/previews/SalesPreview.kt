package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.CanceledSalesContent
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesOptionsScreen
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SalesOptionsDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SalesOptionsScreen(
                onOrdersOptionClick = {},
                onStockOptionClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CanceledSalesDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CanceledSalesContent(
                canceledList = canceledList,
                menuItems = arrayOf(),
                onMoreOptionsItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CanceledSalesPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CanceledSalesContent(
                canceledList = canceledList,
                menuItems = arrayOf(),
                onMoreOptionsItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val canceledList = listOf(
    CommonItem(id = 1, photo = byteArrayOf(), title = "Homem", subtitle = "R$77.90", description = "Bruno Barbosa"),
    CommonItem(id = 2, photo = byteArrayOf(), title = "Essencial", subtitle = "R$157.99", description = "Daniela"),
    CommonItem(id = 3, photo = byteArrayOf(), title = "Kaiak", subtitle = "R$88.90", description = "Brenda"),
    CommonItem(id = 4, photo = byteArrayOf(), title = "Luna", subtitle = "R$67.90", description = "Fernando"),
    CommonItem(id = 5, photo = byteArrayOf(), title = "Una", subtitle = "R$99.99", description = "Socorro"),
    CommonItem(id = 6, photo = byteArrayOf(), title = "Essencial", subtitle = "R$157.99", description = "Josué"),
    CommonItem(id = 7, photo = byteArrayOf(), title = "Homem", subtitle = "R$77.90", description = "Helena"),
)