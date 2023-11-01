package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveriesContent
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveryContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DeliveriesDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeliveriesContent(
                deliveries = deliveries,
                menuOptions = emptyArray(),
                onItemClick = {},
                onMenuItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DeliveriesPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeliveriesContent(
                deliveries = deliveries,
                menuOptions = arrayOf(),
                onItemClick = {},
                onMenuItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val deliveries = listOf(
    CommonItem(1L, byteArrayOf(), "Bruno Barbosa", "Essencial", "Rua 15 de novembro"),
    CommonItem(2L, byteArrayOf(), "Josué Barbosa", "Essencial", "Rua 15 de novembro"),
    CommonItem(3L, byteArrayOf(), "Daniela Barbosa", "Homem", "Rua 15 de novembro"),
    CommonItem(4L, byteArrayOf(), "Brenda Barbosa", "Essencial", "Rua 15 de novembro"),
    CommonItem(5L, byteArrayOf(), "Helena Barbosa", "Una", "Rua 15 de novembro"),
    CommonItem(6L, byteArrayOf(), "Socorro Barbosa", "Luna", "Rua 15 de novembro"),
    CommonItem(7L, byteArrayOf(), "Fernando Barbosa", "Kaiak", "Rua 15 de novembro"),
 )

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DeliveryDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeliveryContent(
                name = "",
                address = "",
                phoneNumber = "",
                productName = "",
                price = "",
                deliveryPrice = "",
                shippingDate = "",
                deliveryDate = "",
                delivered = false,
                onDeliveryPriceChange = {},
                onDeliveredChange = {},
                onShippingDateClick = {},
                onDeliveryDateClick = {},
                onOutsideClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DeliveryPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeliveryContent(
                name = "",
                address = "",
                phoneNumber = "",
                productName = "",
                price = "",
                deliveryPrice = "",
                shippingDate = "",
                deliveryDate = "",
                delivered = false,
                onDeliveryPriceChange = {},
                onDeliveredChange = {},
                onShippingDateClick = {},
                onDeliveryDateClick = {},
                onOutsideClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}