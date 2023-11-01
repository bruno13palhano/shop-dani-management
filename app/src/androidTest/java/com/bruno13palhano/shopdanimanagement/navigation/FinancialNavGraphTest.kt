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
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.bruno13palhano.shopdanimanagement.MainActivity
import com.bruno13palhano.shopdanimanagement.ui.navigation.FinancialDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.ITEM_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FinancialNavGraphTest {

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
            navController.navigate(FinancialDestinations.FINANCIAL_MAIN_ROUTE)
        }
    }

    @Test
    fun verifyFinancialNavGraphStartDestination() {
        composeRule.onNodeWithText("Financial Info")
            .assertIsDisplayed()
    }

    @Test
    fun onItemClick_fromFinancialScreen_shouldNavigateToFinancialInfoScreen() {
        val expected = FinancialDestinations.FINANCIAL_INFO_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Financial Info"))
        composeRule.onNodeWithText("Financial Info").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromFinancialScreen_shouldNavigateToCustomersDebitScreen() {
        val expected = FinancialDestinations.FINANCIAL_CUSTOMERS_DEBITS_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Customers Debit"))
        composeRule.onNodeWithText("Customers Debit").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromFinancialScreen_shouldNavigateToStockDebitsScreen() {
        val expected = FinancialDestinations.FINANCIAL_STOCK_DEBITS_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Stock Debits"))
        composeRule.onNodeWithText("Stock Debits").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromFinancialScreen_shouldNavigateToCanceledSalesScreen() {
        val expected = FinancialDestinations.FINANCIAL_CANCELED_SALES_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Canceled Sales"))
        composeRule.onNodeWithText("Canceled Sales").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromFinancialInfoScreen_shouldNavigateToFinancialScreen() {
        val expected = FinancialDestinations.FINANCIAL_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Financial Info"))
        composeRule.onNodeWithText("Financial Info").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromCanceledSalesScreen_shouldNavigateToFinancialScreen() {
        val expected = FinancialDestinations.FINANCIAL_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Canceled Sales"))
        composeRule.onNodeWithText("Canceled Sales").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromStockDebitsScreen_shouldNavigateToFinancialScreen() {
        val expected = FinancialDestinations.FINANCIAL_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Stock Debits"))
        composeRule.onNodeWithText("Stock Debits").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromStockDebitScreen_shouldNavigateToItemScreen() {
        val expected = "${FinancialDestinations.FINANCIAL_STOCK_ITEM_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Stock Debits"))
        composeRule.onNodeWithText("Stock Debits").performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromItemScreen_shouldNavigateToStockDebitScreen() {
        val expected = FinancialDestinations.FINANCIAL_STOCK_DEBITS_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Stock Debits"))
        composeRule.onNodeWithText("Stock Debits").performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromCustomersDebitScreen_shouldNavigateToFinancialScreen() {
        val expected = FinancialDestinations.FINANCIAL_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Customers Debit"))
        composeRule.onNodeWithText("Customers Debit").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromCustomerDebitScreen_shouldNavigateToSaleScreen() {
        val expected = "${FinancialDestinations.FINANCIAL_SALE_ITEM_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Customers Debit"))
        composeRule.onNodeWithText("Customers Debit").performClick()

        composeRule.onNodeWithContentDescription("List of debits")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromSaleScreen_shouldNavigateToCustomerDebitScreen() {
        val expected = FinancialDestinations.FINANCIAL_CUSTOMERS_DEBITS_ROUTE

        composeRule.onNodeWithContentDescription("Financial items")
            .performScrollToNode(hasText("Customers Debit"))
        composeRule.onNodeWithText("Customers Debit").performClick()

        composeRule.onNodeWithContentDescription("List of debits")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}