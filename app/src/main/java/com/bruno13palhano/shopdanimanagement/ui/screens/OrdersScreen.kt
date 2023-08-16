package com.bruno13palhano.shopdanimanagement.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun OrdersScreen(
    onSearchClick: () -> Unit,
    onMenuClick: () -> Unit,
    onProductsClick: () -> Unit,
) {
    OrdersContent(
        onSearchClick = onSearchClick,
        onMenuClick = onMenuClick,
        onCategoriesClick = onProductsClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersContent(
    onSearchClick: () -> Unit,
    onMenuClick: () -> Unit,
    onCategoriesClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.orders_label)) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
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
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5F)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 4.dp),
                onClick = onCategoriesClick
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(128.dp),
                        imageVector = Icons.Filled.Checklist,
                        contentDescription = stringResource(id = R.string.category_image_label)
                    )
                    Text(
                        text = stringResource(id = R.string.orders_label),
                        textAlign = TextAlign.Center
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1F)
                    .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 8.dp),
                onClick = {}
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(128.dp),
                        imageVector = Icons.Filled.Analytics,
                        contentDescription = stringResource(id = R.string.analytics_image_label)
                    )
                    Text(
                        text = stringResource(id = R.string.analytics_label),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun RequestsDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            OrdersContent(
                onSearchClick = {},
                onMenuClick = {},
                onCategoriesClick = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun OrdersPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            OrdersContent(
                onSearchClick = {},
                onMenuClick = {},
                onCategoriesClick = {}
            )
        }
    }
}