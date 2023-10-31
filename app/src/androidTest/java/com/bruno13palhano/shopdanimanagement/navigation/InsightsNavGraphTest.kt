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
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.bruno13palhano.shopdanimanagement.MainActivity
import com.bruno13palhano.shopdanimanagement.ui.navigation.InsightsDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class InsightsNavGraphTest {

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
            navController.navigate(InsightsDestinations.INSIGHTS_MAIN_ROUTE)
        }
    }

    @Test
    fun verifyInsightsNavGraphStartDestination() {
        val expected = InsightsDestinations.INSIGHTS_MAIN_ROUTE

        composeRule.onNodeWithText("Insights").assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromInsightsScreen_shouldNavigateToChartsScreen() {
        val expected = InsightsDestinations.INSIGHTS_CHARTS_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .performScrollToNode(hasText("Charts"))
        composeRule.onNodeWithText("Charts").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromChartsScreen_shouldNavigateToInsightsScreen() {
        val expected = InsightsDestinations.INSIGHTS_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .performScrollToNode(hasText("Charts"))
        composeRule.onNodeWithText("Charts").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromInsightsScreen_shouldNavigateToLastSalesScreen() {
        val expected = InsightsDestinations.INSIGHTS_LAST_SALES_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .performScrollToNode(hasText("Last Sales"))
        composeRule.onNodeWithText("Last Sales").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromLastSalesScreen_shouldNavigateToInsightsScreen() {
        val expected = InsightsDestinations.INSIGHTS_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .performScrollToNode(hasText("Last Sales"))
        composeRule.onNodeWithText("Last Sales").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_shouldNavigateToStockOrdersSalesScreen() {
        val expected = InsightsDestinations.INSIGHTS_STOCK_ORDERS_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .performScrollToNode(hasText("Stock vs Orders"))
        composeRule.onNodeWithText("Stock vs Orders").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromStockOrdersSalesScreen_shouldNavigateToInsightsScreen() {
        val expected = InsightsDestinations.INSIGHTS_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .performScrollToNode(hasText("Stock vs Orders"))
        composeRule.onNodeWithText("Stock vs Orders").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_shouldNavigateToSalesByCompanyScreen() {
        val expected = InsightsDestinations.INSIGHTS_COMPANY_SALES_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .performScrollToNode(hasText("Sales by Company"))
        composeRule.onNodeWithText("Sales by Company").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromSalesByCompanyScreen_shouldNavigateToInsightsScreen() {
        val expected = InsightsDestinations.INSIGHTS_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .performScrollToNode(hasText("Sales by Company"))
        composeRule.onNodeWithText("Sales by Company").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}