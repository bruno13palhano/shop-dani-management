package com.bruno13palhano.shopdanimanagement.ui.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainDestinations
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    navController: NavHostController,
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val items = listOf(
        Screen.Home,
        Screen.Financial,
        Screen.Insights,
        Screen.User
    )
    val orientation = LocalConfiguration.current.orientation
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedItem by remember { mutableStateOf(items[0]) }
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape
            ) {
                LazyColumn {
                    stickyHeader {
                        Card(
                            modifier = Modifier.padding(bottom = 16.dp),
                            shape = RectangleShape
                        ) {
                            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                                Image(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .sizeIn(minHeight = 208.dp),
                                    imageVector = Icons.Filled.Image,
                                    contentDescription = stringResource(id = R.string.app_logo_label)
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                                text = stringResource(id = R.string.app_name),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
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
                            selected = currentDestination?.hierarchy?.any { destination ->
                                destination.route == screen.route
                            } == true,
                            onClick = {
                                selectedItem = screen
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
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
private fun DrawerPreview() {
    DrawerMenu(
        navController = rememberNavController(),
        drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
        content = {}
    )
}

@Composable
fun BottomMenu(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Products,
        Screen.Customers
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { Text(text = stringResource(id = screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any{ it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    object Home: Screen(MainDestinations.HOME_ROUTE, Icons.Filled.Home, R.string.home_label)
    object Financial: Screen(MainDestinations.FINANCIAL_ROUTE, Icons.Filled.Money, R.string.financial_label)
    object Insights: Screen(MainDestinations.INSIGHTS_ROUTE, Icons.Filled.Insights, R.string.insights_label)
    object Products: Screen(MainDestinations.PRODUCTS_ROUTE, Icons.Filled.PlaylistAdd, R.string.products_label)
    object Customers: Screen(MainDestinations.CUSTOMERS_ROUTE, Icons.Filled.Person, R.string.customers_label)
    object User: Screen(MainDestinations.USER_ROUTE, Icons.Filled.AccountCircle, R.string.account_label)
}

@Composable
fun MoreOptionsMenu(
    items: Array<String>,
    expanded: Boolean,
    onDismissRequest: (expanded: Boolean) -> Unit,
    onClick: (index: Int) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.semantics { contentDescription = "List of options" },
        expanded = expanded,
        onDismissRequest = { onDismissRequest(false) }
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(
                text = { Text(text = item) },
                onClick = {
                    onClick(index)
                    onDismissRequest(false)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBottomSheet(
    categories: List<CategoryCheck>,
    openBottomSheet: Boolean,
    onBottomSheetChange: (close: Boolean) -> Unit,
    onDismissCategory: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onBottomSheetChange(false)
                onDismissCategory()
            },
            sheetState = bottomSheetState,
        ) {
            LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) {
                items(categories) { categoryItem ->
                    ListItem(
                        headlineContent = { Text(text = categoryItem.category) },
                        leadingContent = {
                            var checked by rememberSaveable { mutableStateOf(categoryItem.isChecked) }

                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    categoryItem.isChecked = it
                                    checked = it
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyBottomSheet(
    companies: List<CompanyCheck>,
    openBottomSheet: Boolean,
    onBottomSheetChange: (close: Boolean) -> Unit,
    onDismissCompany: () -> Unit,
    onSelectedItem: (selected: String) -> Unit
) {
    val initialCompany = companies
        .filter { it.isChecked }
        .findLast { it.isChecked }?.name?.company
    val bottomSheetState = rememberModalBottomSheetState()

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onBottomSheetChange(false)
                onDismissCompany()
            },
            sheetState = bottomSheetState
        ) {
            val (selected, onOptionSelected) = rememberSaveable { mutableStateOf(initialCompany)}

            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                companies.forEach { companyItem ->
                    ListItem(
                        headlineContent = { Text(text = companyItem.name.company) },
                        leadingContent = {
                            RadioButton(
                                selected = companyItem.name.company == selected,
                                onClick = {
                                    onOptionSelected(companyItem.name.company)
                                    onSelectedItem(companyItem.name.company)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerBottomSheet(
    customers: List<CustomerCheck>,
    openBottomSheet: Boolean,
    onBottomSheetChange: (close: Boolean) -> Unit,
    onDismissCustomer: () -> Unit,
    onSelectedItem: (selected: String) -> Unit
) {
    val initialCustomer = customers
        .filter { it.isChecked }
        .findLast { it.isChecked }?.name
    val bottomSheetState = rememberModalBottomSheetState()

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onBottomSheetChange(false)
                onDismissCustomer()
            },
            sheetState = bottomSheetState
        ) {
            val (selected, onOptionSelected) = rememberSaveable { mutableStateOf(initialCustomer) }

            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                customers.forEach { customerItem ->
                    ListItem(
                        headlineContent = { Text(text = customerItem.name) },
                        leadingContent = {
                            RadioButton(
                                selected = customerItem.name == selected,
                                onClick = {
                                    onOptionSelected(customerItem.name)
                                    onSelectedItem(customerItem.name)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

data class CategoryCheck(
    var id: Long,
    var category: String,
    var isChecked: Boolean
)

data class CompanyCheck(
    var name: Company,
    var isChecked: Boolean
)

data class CustomerCheck(
    var id: Long,
    var name: String,
    var address: String,
    var phoneNumber: String,
    var isChecked: Boolean
)