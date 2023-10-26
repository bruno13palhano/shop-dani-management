package com.bruno13palhano.shopdanimanagement.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.bruno13palhano.shopdanimanagement.MainActivity
import com.bruno13palhano.shopdanimanagement.ui.navigation.CatalogDestination
import com.bruno13palhano.shopdanimanagement.ui.navigation.DeliveriesDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.OrdersDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.SalesDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.StockDestinations
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainNavGraphTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MainNavGraph(navController = navController) {}
        }
    }

    @Test
    fun verifyStartDestination() {
        composeTestRule.onNodeWithText("Home")
            .assertIsDisplayed()
    }

    @Test
    fun onSalesItemClick_shouldNavigateToSales() {
        val expected = SalesDestinations.MAIN_SALES_ROUTE

        composeTestRule.onNodeWithText("Sales")
            .onParent()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(expected, route)
    }

    @Test
    fun onStockItemClick_shouldNavigateToStock() {
        val expected = StockDestinations.STOCK_MAIN_ROUTE

        composeTestRule.onNodeWithText("Stock")
            .onParent()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(expected, route)
    }

    @Test
    fun onOrdersItemClick_shouldNavigateToOrders() {
        val expected = OrdersDestinations.ORDERS_MAIN_ROUTE

        composeTestRule.onNodeWithText("Orders")
            .onParent()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(expected, route)
    }

    @Test
    fun onShippingItemClick_shouldNavigateToShipping() {
        val expected = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE

        composeTestRule.onNodeWithText("Deliveries")
            .onParent()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(expected, route)
    }

    @Test
    fun onCatalogItemClick_shouldNavigateToCatalog() = runTest {
        val expected = CatalogDestination.CATALOG_MAIN_ROUTE

        composeTestRule.onNodeWithContentDescription("Options")
            .performScrollToNode(hasText("Catalog"))
        composeTestRule.onNodeWithText("Catalog")
            .onParent()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(expected, route)
    }
}