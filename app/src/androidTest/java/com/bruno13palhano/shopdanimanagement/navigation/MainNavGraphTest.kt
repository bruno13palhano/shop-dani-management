package com.bruno13palhano.shopdanimanagement.navigation

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.bruno13palhano.shopdanimanagement.MainActivity
import com.bruno13palhano.shopdanimanagement.ui.components.BottomMenu
import com.bruno13palhano.shopdanimanagement.ui.components.DrawerMenu
import com.bruno13palhano.shopdanimanagement.ui.navigation.CustomersDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.FinancialDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.InsightsDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.ProductsDestinations
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainNavGraphTest {

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
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var showBottomBar by rememberSaveable { mutableStateOf(true) }

                DrawerMenu(drawerState = drawerState, navController = navController) {
                    val coroutineScope = rememberCoroutineScope()

                    Scaffold(
                        bottomBar = { if (showBottomBar) BottomMenu(navController = navController) }
                    ) {
                        MainNavGraph(
                            modifier = Modifier.padding(it),
                            navController = navController,
                            showBottomMenu = {show ->
                                showBottomBar = show
                            }
                        ) {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun homeNavGraph_ShouldNavigateToHomeScreen() {
        val expected = HomeDestinations.HOME_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Home screen").assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun financialNavGraph_shouldNavigateToFinancialScreen() {
        val expected = FinancialDestinations.FINANCIAL_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Drawer Menu").performClick()

        composeRule.onNodeWithText("Financial").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun insightsNavGraph_shouldNavigateToInsightsScreen() {
        val expected = InsightsDestinations.INSIGHTS_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Drawer Menu").performClick()

        composeRule.onNodeWithText("Insights").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun productsNavGraph_shouldNavigateToProductCategoriesScreen() {
        val expected = ProductsDestinations.MAIN_PRODUCTS_ROUTE

        composeRule.onNodeWithText("Products").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun customersNavGraph_shouldNavigateToCustomersScreen() {
        val expected = CustomersDestinations.MAIN_CUSTOMERS_ROUTE

        composeRule.onNodeWithText("Customers").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}