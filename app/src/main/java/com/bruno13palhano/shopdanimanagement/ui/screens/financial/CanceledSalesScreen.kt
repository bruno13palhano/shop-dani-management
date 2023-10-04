package com.bruno13palhano.shopdanimanagement.ui.screens.financial

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.CanceledSalesViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun CanceledSalesScreen(
    navigateUp: () -> Unit,
    viewModel: CanceledSalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCanceledSales()
    }

    val canceledList by viewModel.canceledSales.collectAsStateWithLifecycle()
    val menuItems = arrayOf(
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
                0 -> {
                    viewModel.getCanceledSalesByName(isOrderedAsc = orderedByName)
                    orderedByName = !orderedByName
                }
                1 -> {
                    viewModel.getCanceledSalesByCustomerName(isOrderedAsc = orderedByCustomerName)
                    orderedByCustomerName = !orderedByCustomerName
                }
                2 -> {
                    viewModel.getCanceledSalesByPrice(isOrderedAsc = orderedByPrice)
                    orderedByPrice = !orderedByPrice
                }
                else -> { viewModel.getAllCanceledSales() }
            }
        },
        navigateUp = navigateUp
    )
}

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
                            imageVector = Icons.Filled.ArrowBack,
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

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CanceledSalesDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CanceledSalesContent(
                canceledList = canceledList,
                menuItems = arrayOf(),
                onMoreOptionsItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CanceledSalesPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CanceledSalesContent(
                canceledList = canceledList,
                menuItems = arrayOf(),
                onMoreOptionsItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val canceledList = listOf(
    CommonItem(id = 1, photo = byteArrayOf(), title = "Homem", subtitle = "R$77.90", description = "Bruno Barbosa"),
    CommonItem(id = 2, photo = byteArrayOf(), title = "Essencial", subtitle = "R$157.99", description = "Daniela"),
    CommonItem(id = 3, photo = byteArrayOf(), title = "Kaiak", subtitle = "R$88.90", description = "Brenda"),
    CommonItem(id = 4, photo = byteArrayOf(), title = "Luna", subtitle = "R$67.90", description = "Fernando"),
    CommonItem(id = 5, photo = byteArrayOf(), title = "Una", subtitle = "R$99.99", description = "Socorro"),
    CommonItem(id = 6, photo = byteArrayOf(), title = "Essencial", subtitle = "R$157.99", description = "Josué"),
    CommonItem(id = 7, photo = byteArrayOf(), title = "Homem", subtitle = "R$77.90", description = "Helena"),
)