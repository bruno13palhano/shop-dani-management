package com.bruno13palhano.shopdanimanagement.ui.screens.insights

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleItemList
import com.bruno13palhano.shopdanimanagement.ui.navigation.InsightsRoutes

@Composable
fun InsightsRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (route: InsightsRoutes) -> Unit,
    onIconMenuClick: () -> Unit,
    goHome: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    InsightsScreen(
        onItemClick = onItemClick,
        onIconMenuClick = onIconMenuClick,
        goHome = goHome
    )
}

@Composable
fun InsightsScreen(
    onItemClick: (route: InsightsRoutes) -> Unit,
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
    onItemClick: (route: InsightsRoutes) -> Unit,
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
        val items =
            listOf(
                InsightsInnerScreen.Charts,
                InsightsInnerScreen.LastSales,
                InsightsInnerScreen.StockOrdersSales,
                InsightsInnerScreen.CompanySales
            )
        Column(
            modifier =
                Modifier
                    .semantics { contentDescription = "List of items" }
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

sealed class InsightsInnerScreen<T : InsightsRoutes>(
    val route: T,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    data object Charts : InsightsInnerScreen<InsightsRoutes>(
        route = InsightsRoutes.Charts,
        icon = Icons.Filled.BarChart,
        resourceId = R.string.charts_label
    )

    data object LastSales : InsightsInnerScreen<InsightsRoutes>(
        route = InsightsRoutes.LastSales,
        icon = Icons.Filled.PointOfSale,
        resourceId = R.string.last_sales_label
    )

    data object StockOrdersSales : InsightsInnerScreen<InsightsRoutes>(
        route = InsightsRoutes.StockOrdersSales,
        icon = Icons.AutoMirrored.Filled.List,
        resourceId = R.string.stock_vs_orders_label
    )

    data object CompanySales : InsightsInnerScreen<InsightsRoutes>(
        route = InsightsRoutes.CompanySales,
        icon = Icons.Filled.Factory,
        resourceId = R.string.company_sales_label
    )
}