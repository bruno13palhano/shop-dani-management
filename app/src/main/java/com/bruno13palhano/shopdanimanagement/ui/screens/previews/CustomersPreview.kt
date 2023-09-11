package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomerInfoContent
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomersContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersContent(
                customerList = customerList,
                onItemClick = {},
                onAddButtonClick = {},
                onIconMenuClick = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersContent(
                customerList = customerList,
                onItemClick = {},
                onAddButtonClick = {},
                onIconMenuClick = {}
            )
        }
    }
}

private val customerList = listOf(
    CommonItem(1L, "", "Bruno", "Rua 15 de novembro", description = ""),
    CommonItem(2L, "", "Brenda",  "13 de maio", description = ""),
    CommonItem(3L, "", "Daniela", "Rua do serrote", description = ""),
    CommonItem(4L, "", "Josué", "Rua 15 de novembro", description = ""),
    CommonItem(5L, "", "Helena", "Rua 13 de maio", description = ""),
    CommonItem(6L, "","Socorro","Rua do serrote", description = ""),
    CommonItem(7L, "","Fernando","Rua do serrote", description = ""),
    CommonItem(8L, "","Henrique","Carão", description = ""),
    CommonItem(9L, "", "Bruno","Rua 15 de novembro", description = ""),
)

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerInfoDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomerInfoContent(
                name = "",
                address = "",
                photo = "",
                owingValue = "120.99",
                purchasesValue = "1590.99",
                lastPurchaseValue = "77.99",
                entry = ChartEntryModelProducer(),
                onEditIconClick = {},
                onOutsideClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerInfoPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomerInfoContent(
                name = "",
                address = "",
                photo = "",
                owingValue = "120.99",
                purchasesValue = "1590.99",
                lastPurchaseValue = "77.99",
                entry = ChartEntryModelProducer(),
                onEditIconClick = {},
                onOutsideClick = {},
                navigateUp = {}
            )
        }
    }
}