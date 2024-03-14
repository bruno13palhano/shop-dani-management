package com.bruno13palhano.shopdanimanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.shopdanimanagement.ui.components.BottomMenu
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.components.DrawerMenu
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            ShopDaniManagementTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var showBottomBar by rememberSaveable { mutableStateOf(false) }
                var gesturesEnabled by rememberSaveable { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrawerMenu(
                        navController = navController,
                        drawerState = drawerState,
                        gesturesEnabled = gesturesEnabled
                    ) {
                        val coroutineScope = rememberCoroutineScope()

                        Scaffold(
                            bottomBar = {
                                AnimatedVisibility(
                                    visible = showBottomBar,
                                    enter = slideInVertically(
                                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                                        initialOffsetY = { it/8 }
                                    ),
                                    exit = slideOutVertically(
                                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                                        targetOffsetY = { it/8 }
                                    )
                                ) {
                                    BottomMenu(navController = navController)
                                }
                            }
                        ) {
                            MainNavGraph(
                                modifier = Modifier.padding(it),
                                navController = navController,
                                showBottomMenu = { show -> showBottomBar = show },
                                gesturesEnabled = { enabled -> gesturesEnabled = enabled }
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
}