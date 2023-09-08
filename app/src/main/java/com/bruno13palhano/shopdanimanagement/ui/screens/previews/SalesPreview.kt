package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.SalesOptionsScreen
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
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