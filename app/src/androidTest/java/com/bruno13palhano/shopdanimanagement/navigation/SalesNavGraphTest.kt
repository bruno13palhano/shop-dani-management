package com.bruno13palhano.shopdanimanagement.navigation

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import com.bruno13palhano.shopdanimanagement.MainActivity
import com.bruno13palhano.shopdanimanagement.ui.navigation.EDITABLE
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.IS_ORDERED
import com.bruno13palhano.shopdanimanagement.ui.navigation.ITEM_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.home.SalesDestinations
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SalesNavGraphTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()

        composeRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            ShopDaniManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavGraph(navController = navController) { }
                }
            }
            navController.navigate(SalesDestinations.MAIN_SALES_ROUTE)
        }
    }

    @Test
    fun verifySalesNavGraphStartDestination() {
        val expected = SalesDestinations.MAIN_SALES_ROUTE

        composeRule.onNodeWithText("Sales").assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromSalesScreen_shouldNavigateToSalesScreen() {
        val expected = "${SalesDestinations.SALES_SALE_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Edit").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onAddButtonClick_fromSalesScreen_shouldNavigateToSalesOptionsScreen() {
        val expected = SalesDestinations.SALES_OPTIONS_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromSalesScreen_shouldNavigateToHomeScreen() {
        val expected = HomeDestinations.HOME_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onOrdersOptionClick_fromSalesOptionsScreen_shouldNavigateToProductListScreen() {
        val expected = SalesDestinations.SALES_PRODUCTS_LIST_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.onNodeWithText("Orders").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onStockOptionClick_fromSalesOptionScreen_shouldNavigateToStockOrdersScreen() {
        val expected = SalesDestinations.SALES_STOCK_LIST_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.onNodeWithText("Stock").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromSalesOptionsScreen_shouldNavigateToSalesScreen() {
        val expected = SalesDestinations.MAIN_SALES_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromStockOrderSearchScreen_shouldNavigateToSaleScreen() {
        val expected = "${SalesDestinations.SALES_SALE_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Stock").performClick()
        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithText("Search for products").performClick()
        composeRule.onNodeWithContentDescription("List of search")
            .onChildren()
            .onLast()
            .performClick()
        Espresso.closeSoftKeyboard()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromStockOrderSearchScreen_shouldNavigateToStockOrdersScreen() {
        val expected = SalesDestinations.SALES_STOCK_LIST_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Stock").performClick()
        composeRule.onNodeWithContentDescription("Search").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromStockOrdersScreen_shouldNavigateToSaleScreen() {
        val expected = "${SalesDestinations.SALES_SALE_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Stock").performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onSearchClick_fromStockOrdersScreen_shouldNavigateToStockOrderSearchScreen() {
        val expected = SalesDestinations.SALES_SEARCH_STOCK_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Stock").performClick()

        composeRule.onNodeWithContentDescription("Search").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromStockOrdersScreen_shouldNavigateToSalesOptionScreen() {
        val expected = SalesDestinations.SALES_OPTIONS_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Stock").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromProductListScreen_shouldNavigateToSaleScreen() {
        val expected = "${SalesDestinations.SALES_SALE_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Orders").performClick()
        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onSearchClick_fromProductListScreen_shouldNavigateToSearchProductScreen() {
        val expected = SalesDestinations.SALES_SEARCH_PRODUCT_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Orders").performClick()

        composeRule.onNodeWithContentDescription("Search").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromProductListScreen_shouldNavigateToSalesOptionsScreen() {
        val expected = SalesDestinations.SALES_OPTIONS_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Orders").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromSearchProductScreen_shouldNavigateToSaleScreen() {
        val expected = "${SalesDestinations.SALES_SALE_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Orders").performClick()
        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithText("Search for products").performClick()
        composeRule.onNodeWithContentDescription("List of search")
            .onChildren()
            .onLast()
            .performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromSearchProductScreen_shouldNavigateToProductListScreen() {
        val expected = SalesDestinations.SALES_PRODUCTS_LIST_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("Orders").performClick()
        composeRule.onNodeWithContentDescription("Search").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}