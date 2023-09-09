package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CommonItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel.DeliveriesViewModel

@Composable
fun DeliveriesScreen(
    navigateUp: () -> Unit,
    onItemClick: (id: Long) -> Unit,
    viewModel: DeliveriesViewModel = hiltViewModel()
) {
    val deliveries by viewModel.deliveries.collectAsStateWithLifecycle()
    DeliveriesContent(
        deliveries = deliveries,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveriesContent(
    deliveries: List<CommonItem>,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
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
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(items = deliveries, key = { delivery -> delivery.id }) { delivery ->
                CommonItemList(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    title = delivery.title,
                    subtitle = delivery.subtitle,
                    description = delivery.description,
                    onClick = { onItemClick(delivery.id) }
                )
            }
        }
    }
}
