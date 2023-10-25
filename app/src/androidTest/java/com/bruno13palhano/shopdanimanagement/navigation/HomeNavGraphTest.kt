package com.bruno13palhano.shopdanimanagement.navigation

import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.printToLog
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeNavGraphTest {

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
    fun onCatalogItemClick_shouldNavigateToCatalog() {
        val expected = CatalogDestination.CATALOG_MAIN_ROUTE

        val a = composeTestRule.onNodeWithContentDescription("Lista")
            .performScrollToKey(5).performScrollTo()

        a.performClick()

        composeTestRule.onRoot().printToLog("Tag")

//        val route = navController.currentBackStackEntry?.destination?.route
//        assertEquals(expected, route)
    }
}