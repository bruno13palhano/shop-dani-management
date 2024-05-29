package com.bruno13palhano.shopdanimanagement.ui.screens.financial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.CanceledSalesViewModel

@Composable
fun CanceledSalesRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    CanceledSalesScreen(navigateUp = navigateUp)
}

@Composable
fun CanceledSalesScreen(
    navigateUp: () -> Unit,
    viewModel: CanceledSalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCanceledSales()
    }

    val canceledList by viewModel.canceledSales.collectAsStateWithLifecycle()
    val menuItems =
        arrayOf(
            stringResource(id = R.string.ordered_by_name_label),
            stringResource(id = R.string.ordered_by_customer_name_label),
            stringResource(id = R.string.ordered_by_price_label),
            stringResource(id = R.string.ordered_by_last_label)
        )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByCustomerName by remember { mutableStateOf(false) }
    var orderedByPrice by remember { mutableStateOf(false) }

    CanceledSalesContent(
        canceledList = canceledList,
        menuItems = menuItems,
        onMoreOptionsItemClick = { index ->
            when (index) {
                CanceledSalesMenu.SALES_BY_NAME -> {
                    viewModel.getCanceledSalesByName(isOrderedAsc = orderedByName)
                    orderedByName = toggleOrdered(orderedByName)
                }
                CanceledSalesMenu.SALES_BY_CUSTOMER_NAME -> {
                    viewModel.getCanceledSalesByCustomerName(isOrderedAsc = orderedByCustomerName)
                    orderedByCustomerName = toggleOrdered(orderedByCustomerName)
                }
                CanceledSalesMenu.SALE_BY_PRICE -> {
                    viewModel.getCanceledSalesByPrice(isOrderedAsc = orderedByPrice)
                    orderedByPrice = toggleOrdered(orderedByPrice)
                }
                CanceledSalesMenu.ALL_CANCELED_SALES -> {
                    viewModel.getAllCanceledSales()
                }
            }
        },
        navigateUp = navigateUp
    )
}

private fun toggleOrdered(ordered: Boolean) = !ordered

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanceledSalesContent(
    canceledList: List<CommonItem>,
    menuItems: Array<String>,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.canceled_sales_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.drawer_menu_label)
                            )
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MoreOptionsMenu(
                                    items = menuItems,
                                    expanded = expanded,
                                    onDismissRequest = { expandedValue ->
                                        expanded = expandedValue
                                    },
                                    onClick = onMoreOptionsItemClick
                                )
                            }
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(items = canceledList, key = { canceledSale -> canceledSale.id }) { canceledSale ->
                HorizontalItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = canceledSale.title,
                    photo = canceledSale.photo,
                    subtitle = canceledSale.subtitle,
                    description = canceledSale.description,
                    onClick = {}
                )
            }
        }
    }
}

private object CanceledSalesMenu {
    const val SALES_BY_NAME = 0
    const val SALES_BY_CUSTOMER_NAME = 1
    const val SALE_BY_PRICE = 2
    const val ALL_CANCELED_SALES = 3
}