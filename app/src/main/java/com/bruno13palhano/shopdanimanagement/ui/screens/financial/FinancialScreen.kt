package com.bruno13palhano.shopdanimanagement.ui.screens.financial

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CancelPresentation
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Payments
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
import com.bruno13palhano.shopdanimanagement.ui.navigation.FinancialRoutes

@Composable
fun FinancialRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (route: FinancialRoutes) -> Unit,
    onIconMenuClick: () -> Unit,
    goHome: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    FinancialScreen(
        onItemClick = onItemClick,
        onIconMenuClick = onIconMenuClick,
        goHome = goHome
    )
}

@Composable
fun FinancialScreen(
    onItemClick: (route: FinancialRoutes) -> Unit,
    onIconMenuClick: () -> Unit,
    goHome: () -> Unit
) {
    FinancialContent(
        onItemClick = onItemClick,
        onIconMenuClick = onIconMenuClick,
        goHome = goHome
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialContent(
    onItemClick: (route: FinancialRoutes) -> Unit,
    onIconMenuClick: () -> Unit,
    goHome: () -> Unit
) {
    BackHandler(enabled = true, onBack = goHome)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.financial_label)) },
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
                FinancialInnerScreen.Info,
                FinancialInnerScreen.CustomersDebit,
                FinancialInnerScreen.StockDebits,
                FinancialInnerScreen.CanceledSales
            )
        Column(
            modifier =
                Modifier
                    .semantics { contentDescription = "Financial items" }
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

sealed class FinancialInnerScreen<T : FinancialRoutes>(
    val route: T,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    data object Info : FinancialInnerScreen<FinancialRoutes>(
        route = FinancialRoutes.Info,
        icon = Icons.Filled.Info,
        resourceId = R.string.financial_info_label
    )

    data object CustomersDebit : FinancialInnerScreen<FinancialRoutes>(
        route = FinancialRoutes.CustomersDebits,
        icon = Icons.Filled.Payments,
        resourceId = R.string.customers_debit_label
    )

    data object StockDebits : FinancialInnerScreen<FinancialRoutes>(
        route = FinancialRoutes.StockDebits,
        icon = Icons.Filled.Inventory,
        resourceId = R.string.stock_debits_label
    )

    data object CanceledSales : FinancialInnerScreen<FinancialRoutes>(
        route = FinancialRoutes.CanceledSales,
        icon = Icons.Filled.CancelPresentation,
        resourceId = R.string.canceled_sales_label
    )
}