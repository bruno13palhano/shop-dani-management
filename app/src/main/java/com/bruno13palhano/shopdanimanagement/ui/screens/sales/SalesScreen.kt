package com.bruno13palhano.shopdanimanagement.ui.screens.sales

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
import com.bruno13palhano.shopdanimanagement.ui.components.ExpandedItem
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SalesViewModel

@Composable
fun SalesScreen(
    screenTitle: String,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: SalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getSales()
    }

    val saleList by viewModel.saleList.collectAsStateWithLifecycle()
    val menuItems = arrayOf(
        stringResource(id = R.string.ordered_by_name_label),
        stringResource(id = R.string.ordered_by_price_label),
        stringResource(id = R.string.ordered_by_last_label)
    )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByPrice by remember { mutableStateOf(false) }

    SalesContent(
        screenTitle = screenTitle,
        saleList = saleList,
        menuItems = menuItems,
        onItemClick = onItemClick,
        onMoreOptionsItemClick = { index ->
            when (index) {
                0 -> {
                    viewModel.getSalesByCustomerName(isOrderedAsc = orderedByName)
                    orderedByName = !orderedByName
                }
                1 -> {
                    viewModel.getSalesBySalePrice(isOrderedAsc = orderedByPrice)
                    orderedByPrice = !orderedByPrice
                }
                else -> { viewModel.getSales() }
            }
        },
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesContent(
    screenTitle: String,
    saleList: List<CommonItem>,
    menuItems: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
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
            FloatingActionButton(onClick = onAddButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .semantics { contentDescription = "List of items" }
                .padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(items = saleList, key = { item -> item.id }) { item ->
                ExpandedItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    subtitle = item.subtitle,
                    description = stringResource(
                        id = R.string.date_of_sale_tag,
                        dateFormat.format(item.description.toLong())
                    ),
                    photo = item.photo,
                    onEditClick = { onItemClick(item.id) }
                )
            }
        }
    }
}