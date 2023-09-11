package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CommonItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel.DeliveriesViewModel

@Composable
fun DeliveriesScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: DeliveriesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getDeliveries(false)
    }
    val deliveries by viewModel.deliveries.collectAsStateWithLifecycle()

    val menuOptions = arrayOf(
        stringResource(id = R.string.not_delivered_label),
        stringResource(id = R.string.delivered_label),
        stringResource(id = R.string.all_deliveries_label),
    )

    DeliveriesContent(
        deliveries = deliveries,
        onItemClick = onItemClick,
        menuOptions = menuOptions,
        onMenuItemClick = { index ->
            when (index) {
                0 -> {
                    viewModel.getDeliveries(false)
                }
                1 -> {
                    viewModel.getDeliveries(true)
                }
                else -> {
                    viewModel.getAllDeliveries()
                }
            }
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveriesContent(
    deliveries: List<CommonItem>,
    menuOptions: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onMenuItemClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.deliveries_label)) },
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
                                contentDescription = stringResource(id = R.string.more_options_label)
                            )
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MoreOptionsMenu(
                                    items = menuOptions,
                                    expanded = expanded,
                                    onDismissRequest = { expanded = it },
                                    onClick = onMenuItemClick
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
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(items = deliveries, key = { delivery -> delivery.id }) { delivery ->
                CommonItemList(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    title = delivery.title,
                    subtitle = delivery.subtitle,
                    description = delivery.description,
                    onClick = { onItemClick(delivery.id) }
                )
            }
        }
    }
}
