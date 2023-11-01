package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.home.HomeContent
import com.bruno13palhano.shopdanimanagement.ui.screens.home.HomeViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeContent(
                homeInfo = HomeViewModel.HomeInfo(),
                lastSalesEntry = ChartEntryModelProducer(),
                onOptionsItemClick = {},
                onSalesItemClick = { _, _ ->},
                onMenuClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeContent(
                homeInfo = HomeViewModel.HomeInfo(),
                lastSalesEntry = ChartEntryModelProducer(),
                onOptionsItemClick = {},
                onSalesItemClick = { _, _ -> },
                onMenuClick = {}
            )
        }
    }
}