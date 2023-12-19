package com.bruno13palhano.shopdanimanagement.ui.components

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListContent(
    isAddButtonEnabled: Boolean,
    screenTitle: String,
    itemList: List<Stock>,
    menuOptions: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onMenuItemClick: (index: Int) -> Unit,
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
        },
        floatingActionButton = {
            if (isAddButtonEnabled) {
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
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        ) {
            items(items = itemList, key = { item -> item.id }) { item ->
                HorizontalItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.name,
                    subtitle = stringResource(id = R.string.price_tag, item.purchasePrice),
                    description = stringResource(id = R.string.quantity_tag, item.quantity),
                    photo = item.photo,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}