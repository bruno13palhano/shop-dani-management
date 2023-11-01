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
import com.bruno13palhano.shopdanimanagement.ui.navigation.ITEM_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.OrdersDestinations
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class OrdersNavGraphTest {

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
            navController.navigate(OrdersDestinations.ORDERS_MAIN_ROUTE)
        }
    }

    @Test
    fun verifyOrdersNavGraphStartDestination() {
        val expected = OrdersDestinations.ORDERS_MAIN_ROUTE

        composeRule.onNodeWithText("Orders List").assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromStockOrdersScreen_shouldNavigateToItemScreen() {
        val expected = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onSearchClick_fromStockOrdersScreen_shouldNavigateToStockOrderSearchScreen() {
        val expected = OrdersDestinations.ORDERS_SEARCH_ITEM_ROUTE

        composeRule.onNodeWithContentDescription("Search").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromStockOrdersScreen_shouldNavigateToHomeScreen() {
        val expected = OrdersDestinations.ORDERS_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Search").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromStockOrderSearchScreen_shouldNavigateToItemScreen() {
        val expected = "${OrdersDestinations.ORDERS_EDIT_ITEM_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithText("Search for products").performClick()
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithContentDescription("List of search")
            .onChildren()
            .onLast()
            .performClick()

        composeRule.onNodeWithContentDescription("List of items").assertIsDisplayed()
            .onChildren()
            .onFirst()
            .performClick()


        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromStockOrderSearchScreen_shouldNavigateToStockOrderScreen() {
        val expected = OrdersDestinations.ORDERS_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithText("Search for products").performClick()
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithContentDescription("List of search")
            .onChildren()
            .onLast()
            .performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}