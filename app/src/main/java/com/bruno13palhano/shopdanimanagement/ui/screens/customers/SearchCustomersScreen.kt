package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.SearchCustomersViewModel

@Composable
fun SearchCustomersRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    SearchCustomersScreen(
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@Composable
fun SearchCustomersScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: SearchCustomersViewModel = hiltViewModel()
) {
    val customers by viewModel.customers.collectAsStateWithLifecycle()
    val searchCacheList by viewModel.searchCache.collectAsStateWithLifecycle()

    SearchCustomersContent(
        customers = customers,
        searchCacheList = searchCacheList,
        onSearchClick = { search ->
            viewModel.search(search = search)
            viewModel.insertCache(search = search)
        },
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCustomersContent(
    customers: List<CommonItem>,
    searchCacheList: List<SearchCache>,
    onSearchClick: (search: String) -> Unit,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = {})}) {
        SearchBar(
            modifier = Modifier
                .semantics { contentDescription = "Search bar" }
                .fillMaxWidth(),
            query = search,
            onQueryChange = { searchValue -> search = searchValue },
            onSearch = { searchValue ->
                active = false
                onSearchClick(searchValue)
            },
            active = active,
            onActiveChange = { isActive -> active = isActive },
            leadingIcon = {
                IconButton(onClick = { if (active) active = false else navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                modifier = Modifier
                    .semantics { contentDescription = "List of search" }
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(
                    items = searchCacheList,
                    key = { searchCache -> searchCache.search }
                ) { searchCache ->
                    SimpleItemList(
                        modifier = Modifier.padding(1.dp),
                        itemName = searchCache.search,
                        imageVector = Icons.Filled.Close,
                        onClick = {
                            active = false
                            onSearchClick(searchCache.search)
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .semantics { contentDescription = "List of customers" }
                .padding(it),
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