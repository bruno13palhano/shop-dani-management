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
import com.bruno13palhano.shopdanimanagement.ui.navigation.EDITABLE
import com.bruno13palhano.shopdanimanagement.ui.navigation.ITEM_ID
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.navigation.ProductsDestinations
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ProductsNavGraphTest {

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
            navController.navigate(ProductsDestinations.MAIN_PRODUCTS_ROUTE)
        }
    }

    @Test
    fun verifyProductsNavGraphStartDestination() {
        val expected = ProductsDestinations.MAIN_PRODUCTS_ROUTE

        composeRule.onNodeWithText("Product Categories").assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromProductCategoriesScreen_shouldNavigateToProductListScreen() {
        val expected = "${ProductsDestinations.PRODUCTS_LIST_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromProductListScreen_shouldNavigateToProductScreen() {
        val expected = "${ProductsDestinations.PRODUCTS_PRODUCT_ROUTE}/{$ITEM_ID}/{$EDITABLE}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onSearchClick_fromProductListScreen_shouldNavigateToSearchProductScreen() {
        val expected = "${ProductsDestinations.PRODUCTS_SEARCH_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Search").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onAddButtonClick_fromProductListScreen_shouldNavigateToProductScreen() {
        val expected = "${ProductsDestinations.PRODUCTS_PRODUCT_ROUTE}/{$ITEM_ID}/{$EDITABLE}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Add").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromProductListScreen_shouldNavigateToProductCategoriesScreen() {
        val expected = ProductsDestinations.MAIN_PRODUCTS_ROUTE

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onItemClick_fromSearchProductScreen_shouldNavigateToProductScreen() {
        val expected = "${ProductsDestinations.PRODUCTS_PRODUCT_ROUTE}/{$ITEM_ID}/{$EDITABLE}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()
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
        val expected = "${ProductsDestinations.PRODUCTS_LIST_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()
        composeRule.onNodeWithContentDescription("Search").performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onAddToCatalogClick_fromProductScreen_shouldNavigateToCatalogItemScreen() {
        val expected = "${ProductsDestinations.PRODUCTS_CATALOG_NEW_ITEM_ROUTE}/{$ITEM_ID}/{$EDITABLE}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("More options").performClick()
        composeRule.onNodeWithContentDescription("List of options")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromProductScreen_shouldNavigateToProductListScreen() {
        val expected = "${ProductsDestinations.PRODUCTS_LIST_ROUTE}/{$ITEM_ID}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }

    @Test
    fun onNavigateUp_fromCatalogItemScreen_shouldNavigateToProductScreen() {
        val expected = "${ProductsDestinations.PRODUCTS_PRODUCT_ROUTE}/{$ITEM_ID}/{$EDITABLE}"

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("List of items")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("More options").performClick()
        composeRule.onNodeWithContentDescription("List of options")
            .onChildren()
            .onFirst()
            .performClick()

        composeRule.onNodeWithContentDescription("Up button").performClick()

        val route = navController.currentBackStackEntry?.destination?.route

        assertEquals(expected, route)
    }
}