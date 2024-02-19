package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.core.model.CustomerInfo
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomerInfoContent
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomersContent
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.CustomersDebitContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomerDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersContent(
                customerList = customerList,
                customerInfo = CustomerInfo.emptyCustomerInfo(),
                openCustomerBottomSheet = false,
                chartEntries = ChartEntryModelProducer(),
                menuItems = arrayOf(),
                onItemClick = {},
                onSearchClick = {},
                onMoreOptionsItemClick = {},
                onEditCustomerClick = {},
                onDismissCustomerBottomSheet = {},
                onAddButtonClick = {},
                onIconMenuClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
                customerInfo = CustomerInfo.emptyCustomerInfo(),
                openCustomerBottomSheet = false,
                chartEntries = ChartEntryModelProducer(),
                menuItems = arrayOf(),
                onItemClick = {},
                onSearchClick = {},
                onMoreOptionsItemClick = {},
                onEditCustomerClick = {},
                onDismissCustomerBottomSheet = {},
                onAddButtonClick = {},
                onIconMenuClick = {}
            )
        }
    }
}

private val customerList = listOf(
    CommonItem(1L, byteArrayOf(), "Bruno", "Rua 15 de novembro", ""),
    CommonItem(2L, byteArrayOf(), "Brenda",  "13 de maio", ""),
    CommonItem(3L, byteArrayOf(), "Daniela", "Rua do serrote", ""),
    CommonItem(4L, byteArrayOf(), "Josué", "Rua 15 de novembro", ""),
    CommonItem(5L, byteArrayOf(), "Helena", "Rua 13 de maio", ""),
    CommonItem(6L, byteArrayOf(),"Socorro","Rua do serrote", ""),
    CommonItem(7L, byteArrayOf(),"Fernando","Rua do serrote", ""),
    CommonItem(8L, byteArrayOf(),"Henrique","Carão", ""),
    CommonItem(9L, byteArrayOf(), "Bruno","Rua 15 de novembro", ""),
)

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
                photo = byteArrayOf(),
                owingValue = "120.99",
                purchasesValue = "1590.99",
                lastPurchaseValue = "77.99",
                entry = ChartEntryModelProducer(),
                onEditIconClick = {},
                onOutsideClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
                photo = byteArrayOf(),
                owingValue = "120.99",
                purchasesValue = "1590.99",
                lastPurchaseValue = "77.99",
                entry = ChartEntryModelProducer(),
                onEditIconClick = {},
                onOutsideClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomersDebitDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersDebitContent(
                debits = debits,
                menuItems = arrayOf(),
                onItemClick = {},
                onMoreOptionsItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomersDebitPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersDebitContent(
                debits = debits,
                menuItems = arrayOf(),
                onItemClick = {},
                onMoreOptionsItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val debits = listOf(
    CommonItem(1L, byteArrayOf(), "Bruno", "R$12.50", "Feb 4, 2023"),
    CommonItem(2L, byteArrayOf(), "Daniela", "R$170.90", "Feb 7, 2023"),
    CommonItem(3L, byteArrayOf(), "Josué", "R$165.99", "Feb 8, 2023"),
    CommonItem(4L, byteArrayOf(), "Helena", "R$9.90", "Feb 11, 2023"),
    CommonItem(5L, byteArrayOf(), "Fernando", "R$160.50", "Feb 12, 2023"),
    CommonItem(6L, byteArrayOf(), "Socorro","R$122.50", "Feb 14, 2023"),
    CommonItem(7L, byteArrayOf(), "Brenda", "R$282.99", "Feb 14, 2023"),
)