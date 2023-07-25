package com.bruno13palhano.shopdanimanagement.ui.theme.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.shopdanimanagement.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val items = listOf(
        Screen.Home,
        Screen.Stock,
        Screen.Shopping,
        Screen.Sales,
        Screen.Requests,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedItem by remember { mutableStateOf(items[0]) }
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape
            ) {
                LazyColumn() {
                    stickyHeader {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth(),
                            text = "ShopDani Management",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    items(
                        items = items
                    ) { screen ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(imageVector = screen.icon, contentDescription = null)
                            },
                            label = {
                                Text(text = stringResource(id = screen.resourceId))
                            },
                            selected = screen.route == selectedItem.route,
                            onClick = {
                                selectedItem = screen
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        },
        content = content
    )
}

@Composable
@Preview(showBackground = true)
fun P() {
    DrawerMenu(
        navController = rememberNavController(),
        drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
    ) {

    }
}

sealed class Screen(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    object Home: Screen(MainDestinations.HOME_ROUTE, Icons.Filled.Home, R.string.app_name)
    object Stock: Screen(MainDestinations.STOCK_ROUTE, Icons.Filled.List, R.string.stock_label)
    object Shopping: Screen(MainDestinations.SHOPPING_ROUTE, Icons.Filled.ShoppingCart, R.string.shopping_label)
    object Sales: Screen(MainDestinations.SALES_ROUTE, Icons.Filled.PointOfSale, R.string.sales_label)
    object Requests: Screen(MainDestinations.REQUESTS_ROUTE, Icons.Filled.Checklist, R.string.requests_label)
}