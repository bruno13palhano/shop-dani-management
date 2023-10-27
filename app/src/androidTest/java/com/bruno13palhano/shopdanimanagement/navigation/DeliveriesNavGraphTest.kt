package com.bruno13palhano.shopdanimanagement.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.bruno13palhano.shopdanimanagement.MainActivity
import com.bruno13palhano.shopdanimanagement.ui.navigation.DeliveriesDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ITEM_ID = "item_Id"

@HiltAndroidTest
class DeliveriesNavGraphTest {

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
            navController.navigate(DeliveriesDestinations.DELIVERIES_MAIN_ROUTE)
        }
    }

    @Test
    fun verifyDeliveriesNavGraphStartDestination() {
        val expected = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE

        composeRule.onNodeWithText("Deliveries")
            .assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromDeliveriesScreen_shouldNavigateToHomeScreen() {
        val expected = HomeDestinations.HOME_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromDeliveriesScreen_shouldNavigateToDeliveryScreen() {
        val expected = "${DeliveriesDestinations.DELIVERIES_DELIVERY_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("List of deliveries")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromDeliveryScreen_shouldNavigateToDeliveriesScreen() {
        val expected = DeliveriesDestinations.DELIVERIES_MAIN_ROUTE

        composeRule.onNodeWithContentDescription("List of deliveries")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}