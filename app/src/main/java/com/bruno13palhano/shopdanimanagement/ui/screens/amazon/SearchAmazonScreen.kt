package com.bruno13palhano.shopdanimanagement.ui.screens.amazon

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.viewmodel.SearchAmazonViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem

@Composable
fun SearchAmazonRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    SearchAmazonScreen(
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@Composable
fun SearchAmazonScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: SearchAmazonViewModel = hiltViewModel()
) {
    val amazonSale by viewModel.amazonSales.collectAsStateWithLifecycle()
    val searchCacheList by viewModel.searchCache.collectAsStateWithLifecycle()

    SearchAmazonContent(
        amazonSales = amazonSale,
        searchCacheList = searchCacheList,
        onSearchClick = viewModel::search,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAmazonContent(
    amazonSales: List<CommonItem>,
    searchCacheList: List<SearchCache>,
    onSearchClick: (search: String) -> Unit,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
) {
    var search by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = {}) }) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = search,
            onQueryChange = { searchValue -> search = searchValue },
            onSearch = {  searchValue ->
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
            placeholder = { Text(text = stringResource(id = R.string.search_label)) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                reverseLayout = true
            ) {
                items(
                    items = searchCacheList,
                    key = { searchCache -> searchCache.search }
                ) { searchCcache ->
                    SimpleItemList(
                        modifier = Modifier.padding(1.dp),
                        itemName = searchCcache.search,
                        imageVector = Icons.Filled.Close,
                        onClick = {
                            active = false
                            onSearchClick(searchCcache.search)
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(items = amazonSales, key = { item -> item.id }) { item ->
                HorizontalItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    subtitle = item.subtitle,
                    description = item.description,
                    photo = item.photo,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}