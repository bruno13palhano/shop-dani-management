package com.bruno13palhano.shopdanimanagement.navigation

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.bruno13palhano.shopdanimanagement.MainActivity
import com.bruno13palhano.shopdanimanagement.ui.navigation.CatalogDestination
import com.bruno13palhano.shopdanimanagement.ui.navigation.DeliveriesDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.IS_ORDERED
import com.bruno13palhano.shopdanimanagement.ui.navigation.ITEM_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.OrdersDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.SalesDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.StockDestinations
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
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
            ShopDaniManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavGraph(navController = navController) {}
                }
            }
        }
    }

    @Test
    fun verifyStartDestination() {
        composeTestRule.onNodeWithText("Home")
            .assertIsDisplayed()
    }

    @Test
    fun onSalesItemClick_fromHomeScreen_shouldNavigateToSaleScreen() {
        val expected = "${HomeDestinations.HOME_SALE_ROUTE}/{$ITEM_ID}/{$IS_ORDERED}"

        composeTestRule.onNodeWithContentDescription("List of sales")
            .performScrollTo()
            .onChildren()
            .onFirst()
            .performClick()
        composeTestRule.onAllNodesWithContentDescription("Expand item")
            .onFirst()
            .performClick()

        composeTestRule.onAllNodesWithContentDescription("Edit")
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromSaleScreen_shouldNavigateToHomeScreen() {
        val expected = HomeDestinations.HOME_MAIN_ROUTE

        composeTestRule.onNodeWithContentDescription("List of sales")
            .performScrollTo()
            .onChildren()
            .onFirst()
            .performClick()
        composeTestRule.onAllNodesWithContentDescription("Expand item")
            .onFirst()
            .performClick()
        composeTestRule.onAllNodesWithContentDescription("Edit")
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
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