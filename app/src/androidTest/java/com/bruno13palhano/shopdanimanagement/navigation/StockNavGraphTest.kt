package com.bruno13palhano.shopdanimanagement.navigation

import androidx.activity.compose.setContent
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
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.StockDestinations
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ITEM_ID = "item_Id"
private const val EDITABLE = "item_editable"
private const val IS_ORDERED = "is_ordered"

@HiltAndroidTest
class StockNavGraphTest {

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
            MainNavGraph(navController = navController) { }
            navController.navigate(StockDestinations.STOCK_MAIN_ROUTE)
        }
    }

    @Test
    fun verifyStockNavGraphStartDestination() {
        val expected = StockDestinations.STOCK_MAIN_ROUTE

        composeRule.onNodeWithText("Stock List").assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromStockOrdersScreen_shouldNavigateToItemScreen() {
        val expected = "${StockDestinations.STOCK_ITEM_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onSearchClick_fromStockOrdersScreen_shouldNavigateToStockOrderSearchScreen() {
        val expected = StockDestinations.STOCK_SEARCH_ITEM_ROUTE

        composeRule.onNodeWithContentDescription("Search").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onAddButtonClick_fromStockOrdersScreen_shouldNavigateToProductListScreen() {
        val expected = StockDestinations.STOCK_ITEM_LIST_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromStockOrdersScreen_shouldNavigateToHomeScreen() {
        val expected = HomeDestinations.HOME_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromStockOrderSearchScreen_shouldNavigateToItemScreen() {
        val expected = "${StockDestinations.STOCK_ITEM_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithText("Search for products").performClick()
        Espresso.closeSoftKeyboard()

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
    fun onNavigateUp_fromStockOrderSearchScreen_shouldNavigateToStockOrdersScreen() {
        val expected = StockDestinations.STOCK_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Search").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromProductListScreen_shouldNavigateToItemScreen() {
        val expected = "${StockDestinations.STOCK_ITEM_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onSearchClick_fromProductListScreen_shouldNavigateSearchProductScreen() {
        val expected = StockDestinations.STOCK_SEARCH_PRODUCT_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.onNodeWithContentDescription("Search").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromProductListScreen_shouldNavigateToStockOrdersScreen() {
        val expected = StockDestinations.STOCK_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromSearchProductScreen_shouldNavigateToItemScreen() {
        val expected = "${StockDestinations.STOCK_ITEM_ROUTE}/{$ITEM_ID}/{$EDITABLE}/{$IS_ORDERED}"

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithText("Search for products").performClick()
        Espresso.closeSoftKeyboard()
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
        val expected = StockDestinations.STOCK_ITEM_LIST_ROUTE

        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithContentDescription("Search").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}