package com.bruno13palhano.shopdanimanagement.ui.screens.customers

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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CommonPhotoItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomersViewModel

@Composable
fun CustomersScreen(
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onIconMenuClick: () -> Unit,
    viewModel: CustomersViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCustomers()
    }

    val customerList by viewModel.customerList.collectAsStateWithLifecycle()
    val menuItems = arrayOf(
        stringResource(id = R.string.ordered_by_name_label),
        stringResource(id = R.string.ordered_by_address_label),
        stringResource(id = R.string.ordered_by_last_label)
    )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByAddress by remember { mutableStateOf(false) }

    CustomersContent(
        customerList = customerList,
        menuItems = menuItems,
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onMoreOptionsItemClick = { index ->
            when (index) {
                0 -> {
                    viewModel.getOrderedByName(isOrderedAsc = orderedByName)
                    orderedByName = !orderedByName
                }
                1 -> {
                    viewModel.getOrderedByAddress(isOrderedAsc = orderedByAddress)
                    orderedByAddress = !orderedByAddress
                }
                else -> { viewModel.getAllCustomers() }
            }
        },
        onAddButtonClick = onAddButtonClick,
        onIconMenuClick = onIconMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersContent(
    customerList: List<CommonItem>,
    menuItems: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onAddButtonClick: () -> Unit,
    onIconMenuClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customers_label)) },
                navigationIcon = {
                    IconButton(onClick = onIconMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search_label)
                        )
                    }
                    IconButton(onClick = { expanded = true }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.more_options_label)
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
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(items = customerList, key = { item -> item.id }) { item ->
                CommonPhotoItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    subtitle = item.subtitle,
                    photo = item.photo,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}