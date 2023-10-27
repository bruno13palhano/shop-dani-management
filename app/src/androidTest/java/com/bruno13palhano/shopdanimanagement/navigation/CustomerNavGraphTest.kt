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
import com.bruno13palhano.shopdanimanagement.ui.navigation.CustomersDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ITEM_ID = "item_id"
private const val EDITABLE = "item_editable"

@HiltAndroidTest
class CustomerNavGraphTest {

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
            MainNavGraph(
                navController = navController,
            ) {}
            navController.navigate(CustomersDestinations.MAIN_CUSTOMERS_ROUTE)
        }
    }

    @Test
    fun verifyCustomerNavGraphStartDestination() {
        composeTestRule.onNodeWithText("Customers")
            .assertIsDisplayed()
    }

    @Test
    fun onItemClick_fromCustomersScreen_shouldNavigateToCustomerInfoScreen() {
        val expected = "${CustomersDestinations.CUSTOMERS_CUSTOMER_INFO_ROUTE}/{$ITEM_ID}"

        composeTestRule.onNodeWithContentDescription("List of customers")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromCustomerInfoScreen_shouldNavigateToCustomerScreen() {
        val expected = CustomersDestinations.MAIN_CUSTOMERS_ROUTE

        composeTestRule.onNodeWithContentDescription("List of customers")
            .onChildren()
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onSearchClick_fromCustomersScreen_shouldNavigateToSearchCustomerScreen() {
        val expected = CustomersDestinations.CUSTOMERS_SEARCH_ROUTE

        composeTestRule.onNodeWithContentDescription("Search").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromSearchCustomerScreen_shouldNavigateToCustomersScreen() {
        val expected = CustomersDestinations.MAIN_CUSTOMERS_ROUTE

        composeTestRule.onNodeWithContentDescription("Search").performClick()

        composeTestRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onAddButtonClick_fromCustomersScreen_shouldNavigateToCustomerScreen() {
        val expected = "${CustomersDestinations.CUSTOMERS_CUSTOMER_ROUTE}/{$ITEM_ID}/{$EDITABLE}"

        composeTestRule.onNodeWithContentDescription("Add").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromCustomerScreen_shouldNavigateToCustomersScreen() {
        val expected = CustomersDestinations.MAIN_CUSTOMERS_ROUTE

        composeTestRule.onNodeWithContentDescription("Add").performClick()

        composeTestRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_FromSearchCustomersScreen_shouldNavigateToCustomerScreen() {
        val expected = "${CustomersDestinations.CUSTOMERS_CUSTOMER_ROUTE}/{$ITEM_ID}/{$EDITABLE}"

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MainNavGraph(
                navController = navController,
            ) {}
            navController.navigate(CustomersDestinations.CUSTOMERS_SEARCH_ROUTE)
        }

        composeTestRule.onNodeWithContentDescription("Search bar")
            .performClick()

        composeTestRule.onNodeWithContentDescription("List of search")
            .onChildren()
            .onLast()
            .performClick()
        Espresso.closeSoftKeyboard()

        composeTestRule.onNodeWithContentDescription("List of customers")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_FromCustomerScreen_shouldNavigateToSearchCustomersScreen() {
        val expected = CustomersDestinations.CUSTOMERS_SEARCH_ROUTE

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MainNavGraph(
                navController = navController,
            ) {}
            navController.navigate(CustomersDestinations.CUSTOMERS_SEARCH_ROUTE)
        }

        composeTestRule.onNodeWithContentDescription("Search bar")
            .performClick()

        composeTestRule.onNodeWithContentDescription("List of search")
            .onChildren()
            .onLast()
            .performClick()
        Espresso.closeSoftKeyboard()

        composeTestRule.onNodeWithContentDescription("List of customers")
            .onChildren()
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onEditIconClick_FromCustomerInfoScreen_shouldNavigateToCustomerScreen() {
        val expected = "${CustomersDestinations.CUSTOMERS_CUSTOMER_ROUTE}/{$ITEM_ID}/{$EDITABLE}"

        composeTestRule.onNodeWithContentDescription("List of customers")
            .onChildren()
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("Edit").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_FromCustomerScreen_shouldNavigateToCustomerInfoScreen() {
        val expected = "${CustomersDestinations.CUSTOMERS_CUSTOMER_INFO_ROUTE}/{$ITEM_ID}"

        composeTestRule.onNodeWithContentDescription("List of customers")
            .onChildren()
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("Edit").performClick()
        composeTestRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}