package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.DeliveriesContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun DeliveriesDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeliveriesContent(
                deliveries = deliveries,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
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
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val deliveries = listOf(
    CommonItem(1L, "", "Bruno Barbosa", "Essencial", "Rua 15 de novembro"),
    CommonItem(2L, "", "Josué Barbosa", "Essencial", "Rua 15 de novembro"),
    CommonItem(3L, "", "Daniela Barbosa", "Homem", "Rua 15 de novembro"),
    CommonItem(4L, "", "Brenda Barbosa", "Essencial", "Rua 15 de novembro"),
    CommonItem(5L, "", "Helena Barbosa", "Una", "Rua 15 de novembro"),
    CommonItem(6L, "", "Socorro Barbosa", "Luna", "Rua 15 de novembro"),
    CommonItem(7L, "", "Fernando Barbosa", "Kaiak", "Rua 15 de novembro"),
 )