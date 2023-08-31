package com.bruno13palhano.shopdanimanagement.ui.screens.insights

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleItemList
import com.bruno13palhano.shopdanimanagement.ui.navigation.InsightsDestinations
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun InsightsScreen(
    onItemClick: (route: String) -> Unit,
    onIconMenuClick: () -> Unit,
    goHome: () -> Unit
) {
    InsightsContent(
        onItemClick = onItemClick,
        onIconMenuClick = onIconMenuClick,
        goHome = goHome
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsContent(
    onItemClick: (route: String) -> Unit,
    onIconMenuClick: () -> Unit,
    goHome: () -> Unit
) {
    BackHandler(enabled = true, onBack = goHome)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.insights_label)) },
                navigationIcon = {
                    IconButton(onClick = onIconMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
                        )
                    }
                }
            )
        }
    ) {
        val items = listOf(
            InsightsInnerScreen.Charts,
            InsightsInnerScreen.LastSales
        )
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            items.forEach { screen ->
                SimpleItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    itemName = stringResource(id = screen.resourceId),
                    imageVector = screen.icon,
                    onClick = { onItemClick(screen.route) }
                )
            }
        }
    }
}

sealed class InsightsInnerScreen(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    object Charts: InsightsInnerScreen(InsightsDestinations.INSIGHTS_CHARTS_ROUTE, Icons.Filled.BarChart, R.string.charts_label)
    object LastSales: InsightsInnerScreen(InsightsDestinations.INSIGHTS_LAST_SALES_ROUTE, Icons.Filled.PointOfSale, R.string.last_sales_label)
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun InsightsDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            InsightsContent(
                onItemClick = {},
                onIconMenuClick = {},
                goHome = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun InsightsPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            InsightsContent(
                onItemClick = {},
                onIconMenuClick = {},
                goHome = {}
            )
        }
    }
}