package com.bruno13palhano.shopdanimanagement.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Menu
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
import androidx.navigation.NavDestination
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.BottomMenu
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.navigation.StockDestinations

@Composable
fun StockScreen(
    destinationHierarchy: Sequence<NavDestination>,
    onBottomMenuItemClick: (route: String) -> Unit,
    onClick: (route:String) -> Unit,
    onMenuClick: () -> Unit
) {
    StockContent(
        destinationHierarchy = destinationHierarchy,
        onBottomMenuItemClick = onBottomMenuItemClick,
        onClick = onClick,
        onMenuClick = onMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockContent(
    destinationHierarchy: Sequence<NavDestination>,
    onBottomMenuItemClick: (route: String) -> Unit,
    onClick: (route: String) -> Unit,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.stock_label))},
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomMenu(
                destinationsHierarchy = destinationHierarchy,
                onItemClick = onBottomMenuItemClick
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5F)
                    .padding(16.dp),
                onClick = { onClick(StockDestinations.STOCK_CATEGORIES_ROUTE) }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(128.dp),
                        imageVector = Icons.Filled.Category,
                        contentDescription = stringResource(id = R.string.category_image_label)
                    )
                    Text(
                        text = stringResource(id = R.string.categories_label),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StockPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockContent(
                destinationHierarchy = sequenceOf(),
                onBottomMenuItemClick = {},
                onClick = {},
                onMenuClick = {}
            )
        }
    }
}