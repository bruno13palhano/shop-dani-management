package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveriesContent
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveryContent
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
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

private val deliveries =
    listOf(
        Delivery(
            1L,
            1L,
            "Bruno Barbosa",
            "Rua 15 de novembro",
            "991111111",
            "Essencial",
            149.90F, 1.20F,
            0L,
            0L,
            false,
            getCurrentTimestamp()
        ),
        Delivery(
            2L,
            2L,
            "Josué Barbosa",
            "Rua 15 de novembro",
            "992222222",
            "Essencial",
            88.90F,
            1.40F,
            0L,
            0L,
            false,
            getCurrentTimestamp()
        ),
        Delivery(
            3L,
            3L,
            "Daniela Barbosa",
            "Rua 15 de novembro",
            "9933333333",
            "Homem",
            145.90F,
            1.50F,
            0L,
            0L,
            false,
            getCurrentTimestamp()
        )
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