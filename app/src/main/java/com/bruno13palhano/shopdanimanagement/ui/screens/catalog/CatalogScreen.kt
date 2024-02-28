package com.bruno13palhano.shopdanimanagement.ui.screens.catalog

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CatalogItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.viewmodel.CatalogViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ExtendedItem
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun CatalogRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    CatalogScreen(
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@Composable
fun CatalogScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAll()
    }

    val catalogItems by viewModel.catalogItems.collectAsStateWithLifecycle()

    val menuOptions = arrayOf(
        stringResource(id = R.string.ordered_by_name_label),
        stringResource(id = R.string.ordered_by_price_label),
        stringResource(id = R.string.ordered_by_last_label)
    )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByPrice by remember { mutableStateOf(false) }

    CatalogContent(
        catalogItems = catalogItems,
        menuOptions = menuOptions,
        onItemClick = onItemClick,
        onMoreOptionsItemClick = { index ->
            when (index) {
                0 -> {
                    viewModel.getOrderedByName(isOrderedAsc = orderedByName)
                    orderedByName = toggleOrdered(orderedByName)
                }
                1 -> {
                    viewModel.getOrderedByPrice(isOrderedAsc = orderedByPrice)
                    orderedByPrice = toggleOrdered(orderedByPrice)
                }
                else -> { viewModel.getAll() }
            }
        },
        navigateUp = navigateUp
    )
}

private fun toggleOrdered(ordered: Boolean) = !ordered

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogContent(
    catalogItems: List<ExtendedItem>,
    menuOptions: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.catalog_label)) },
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
                                onDismissRequest = { expandedValue -> expanded = expandedValue },
                                onClick = onMoreOptionsItemClick
                            )
                        }
                    }
                }
            )
        }
    ) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .semantics { contentDescription = "List of items" }
                .padding(it),
            columns = StaggeredGridCells.Adaptive(144.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(items = catalogItems, key = { item -> item.id }) { item ->
                CatalogItemList(
                    modifier = Modifier.padding(8.dp),
                    photo = item.photo,
                    title = item.title,
                    firstSubtitle = item.firstSubtitle,
                    secondSubtitle = stringResource(
                        id = R.string.price_text_tag, item.secondSubtitle
                    ),
                    description = item.description,
                    footer = stringResource(id = R.string.discount_tag, item.footer),
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CatalogDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CatalogContent(
                catalogItems = fakeCatalogItems,
                menuOptions = arrayOf(),
                onItemClick = {},
                onMoreOptionsItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CatalogPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CatalogContent(
                catalogItems = fakeCatalogItems,
                menuOptions = arrayOf(),
                onItemClick = {},
                onMoreOptionsItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val fakeCatalogItems = listOf(
    ExtendedItem(1L, byteArrayOf(), "Homem Del Parfum", "High quality", "98.99", "The most sale Parfum", "10"),
    ExtendedItem(2L, byteArrayOf(), "Essencial", "High quality", "154.90", "Top 10 requested", "25"),
    ExtendedItem(3L, byteArrayOf(), "Kaiak", "High quality", "122.44", "Top 10 requested", "5"),
    ExtendedItem(4L, byteArrayOf(), "Luna", "High quality", "100.99", "Light fragrance", "5"),
    ExtendedItem(5L, byteArrayOf(), "Una", "High quality", "88.99", "Light fragrance", "20"),
    ExtendedItem(6L, byteArrayOf(), "Homem", "High quality", "110.90", "New fragrance", "15"),
)