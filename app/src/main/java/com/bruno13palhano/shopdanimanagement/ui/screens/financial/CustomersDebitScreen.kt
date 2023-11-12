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
import androidx.compose.material.icons.filled.ArrowBack
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
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.CustomersDebitViewModel

@Composable
fun CustomersDebitScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: CustomersDebitViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getDebits()
    }

    val debits by viewModel.debits.collectAsStateWithLifecycle()
    val menuItems = arrayOf(
        stringResource(id = R.string.ordered_by_name_label),
        stringResource(id = R.string.ordered_by_price_label),
        stringResource(id = R.string.ordered_by_last_label)
    )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByPrice by remember { mutableStateOf(false) }

    CustomersDebitContent(
        debits = debits,
        menuItems = menuItems,
        onItemClick = onItemClick,
        onMoreOptionsItemClick = { index ->
            when (index) {
                0 -> {
                    viewModel.getDebitByCustomerName(orderedByName)
                    orderedByName = toggleOrdered(orderedByName)
                }
                1 -> {
                    viewModel.getDebitBySalePrice(orderedByPrice)
                    orderedByPrice = toggleOrdered(orderedByPrice)
                }
                else -> { viewModel.getDebits() }
            }
        },
        navigateUp = navigateUp
    )
}

private fun toggleOrdered(ordered: Boolean) = !ordered

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersDebitContent(
    debits: List<CommonItem>,
    menuItems: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customers_debit_label)) },
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
                                    onDismissRequest = { expandedValue -> expanded = expandedValue },
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
            modifier = Modifier
                .semantics { contentDescription = "List of debits" }
                .padding(it),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(items = debits, key = { item -> item.id }) { item ->
                ExpandedItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    photo = item.photo,
                    subtitle = stringResource(id = R.string.customer_debit_tag, item.subtitle),
                    description = stringResource(
                        id = R.string.date_of_payment_tag ,
                        dateFormat.format(item.description.toLong())
                    ),
                    onEditClick = { onItemClick(item.id) }
                )
            }
        }
    }
}