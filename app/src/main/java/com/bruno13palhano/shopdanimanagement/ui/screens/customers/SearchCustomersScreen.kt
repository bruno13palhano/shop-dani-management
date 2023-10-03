package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem

@Composable
fun SearchCustomersScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCustomersContent(
    customers: List<CommonItem>,
    onSearchClick: (search: String) -> Unit,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = {})}) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = search,
            onQueryChange = { searchValue -> search = searchValue },
            onSearch = { searchValue ->
                active = false
                onSearchClick(search)
            },
            active = active,
            onActiveChange = { isActive -> active = isActive },
            leadingIcon = {
                IconButton(onClick = { if (active) active = false else navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.up_button_label)
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        active = false
                        onSearchClick(search)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_label)
                    )
                }
            },
            placeholder = { Text(text = stringResource(id = R.string.search_customers_label)) }
        ) {
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
            ) {
                items(items = customers, key = { customer -> customer.id }) { customer ->
                    HorizontalItemList(
                        title = customer.title,
                        subtitle = customer.subtitle,
                        description = customer.description,
                        photo = customer.photo,
                        onClick = { onItemClick(customer.id) }
                    )
                }
            }
        }
    }
}