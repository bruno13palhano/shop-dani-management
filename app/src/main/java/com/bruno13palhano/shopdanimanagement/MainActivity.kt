package com.bruno13palhano.shopdanimanagement

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.components.DrawerMenu
import com.bruno13palhano.shopdanimanagement.ui.navigation.MainNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopDaniManagementTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrawerMenu(
                        navController = navController,
                        drawerState = drawerState
                    ) {
                        val coroutineScope = rememberCoroutineScope()
                        MainNavGraph(
                            navController = navController
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

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainPreview() {
    ShopDaniManagementTheme {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DrawerMenu(
                navController = navController,
                drawerState = drawerState
            ) {
                val coroutineScope = rememberCoroutineScope()
                MainNavGraph(
                    navController = navController
                ) {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            }
        }
    }
}