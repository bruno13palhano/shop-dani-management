package com.bruno13palhano.shopdanimanagement.ui.screens.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.SingleInputDialog
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ExtendedItem
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SalesViewModel

@Composable
fun SalesScreen(
    isOrders: Boolean,
    screenTitle: String,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: SalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        if (isOrders) {
            viewModel.getOrders()
        } else {
            viewModel.getSales()
        }
    }

    val saleList by viewModel.saleList.collectAsStateWithLifecycle()
    val menuItems = arrayOf(
        stringResource(id = R.string.ordered_by_name_label),
        stringResource(id = R.string.ordered_by_price_label),
        stringResource(id = R.string.ordered_by_last_label),
        stringResource(id = R.string.create_spreadsheet_label)
    )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByPrice by remember { mutableStateOf(false) }
    var showSpreadsheetDialog by remember { mutableStateOf(false) }

    SalesContent(
        isOrders = isOrders,
        screenTitle = screenTitle,
        sheetName = viewModel.sheetName,
        showSpreadsheetDialog = showSpreadsheetDialog,
        saleList = saleList,
        menuItems = menuItems,
        onItemClick = onItemClick,
        onSheetNameChange = viewModel::updateSheetName,
        onDialogOkClick = { viewModel.exportSalesSheet() },
        onMoreOptionsItemClick = { index ->
            when (index) {
                MoreOptions.ORDERED_BY_NAME -> {
                    if (isOrders) {
                        viewModel.getOrdersByCustomerName(isOrderedAsc = orderedByName)
                    } else {
                        viewModel.getSalesByCustomerName(isOrderedAsc = orderedByName)
                    }
                    orderedByName = !orderedByName
                }
                MoreOptions.ORDERED_BY_PRICE -> {
                    if (isOrders) {
                        viewModel.getOrdersBySalePrice(isOrderedAsc = orderedByPrice)
                    } else {
                        viewModel.getSalesBySalePrice(isOrderedAsc = orderedByPrice)
                    }
                    orderedByPrice = !orderedByPrice
                }
                MoreOptions.ORDERED_BY_LAST -> {
                    if (isOrders) {
                        viewModel.getOrders()
                    } else {
                        viewModel.getSales()
                    }
                }
                MoreOptions.CREATE_SPREADSHEET -> { showSpreadsheetDialog = true }
            }
        },
        onDismissDialog = { showSpreadsheetDialog = false },
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesContent(
    isOrders: Boolean,
    screenTitle: String,
    sheetName: String,
    showSpreadsheetDialog: Boolean,
    saleList: List<ExtendedItem>,
    menuItems: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onSheetNameChange: (sheetName: String) -> Unit,
    onDialogOkClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onDismissDialog: () -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
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
        },
        floatingActionButton = {
            if (!isOrders) {
                FloatingActionButton(onClick = onAddButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_label)
                    )
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .semantics { contentDescription = "List of items" }
                .padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp),
            reverseLayout = true
        ) {
            items(items = saleList, key = { item -> item.id }) { item ->
                HorizontalItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    subtitle = stringResource(
                        id = R.string.product_price_text_tag,
                        item.firstSubtitle,
                        item.secondSubtitle
                    ),
                    description = stringResource(
                        id = R.string.date_of_sale_tag,
                        dateFormat.format(item.description.toLong())
                    ),
                    photo = item.photo,
                    onClick = { onItemClick(item.id) }
                )
            }
        }

        AnimatedVisibility(
            visible = showSpreadsheetDialog,
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
            exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
        ) {
            SingleInputDialog(
                dialogTitle = stringResource(id = R.string.spreadsheet_name_label),
                label = stringResource(id = R.string.name_label),
                placeholder = stringResource(id = R.string.enter_name_label),
                input = sheetName,
                onInputChange = onSheetNameChange,
                onOkClick = onDialogOkClick,
                onDismissRequest = onDismissDialog
            )
        }
    }
}

private object MoreOptions {
    const val ORDERED_BY_NAME = 0
    const val ORDERED_BY_PRICE = 1
    const val ORDERED_BY_LAST = 2
    const val CREATE_SPREADSHEET = 3
}