package com.bruno13palhano.shopdanimanagement

import android.app.Application
import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.runner.AndroidJUnitRunner
import com.bruno13palhano.shopdanimanagement.ui.components.BottomMenu
import com.bruno13palhano.shopdanimanagement.ui.components.DrawerMenu
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainUiTest {

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 0)
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltTestRule.inject()

        composeTestRule.activity.setContent {
            ShopDaniManagementTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var showBottomBar by rememberSaveable { mutableStateOf(true) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrawerMenu(
                        navController = navController,
                        drawerState = drawerState
                    ) {
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
    }

    @Test
    fun myTest() {
        composeTestRule.onNodeWithText("Sales").performClick()

        composeTestRule.onNodeWithText("Sales").assertIsDisplayed()
    }
}