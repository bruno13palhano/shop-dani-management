package com.bruno13palhano.shopdanimanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.theme.navigation.DrawerMenu
import com.bruno13palhano.shopdanimanagement.ui.theme.navigation.MainNavGraph
import com.bruno13palhano.shopdanimanagement.ui.theme.screens.HomeScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopDaniManagementTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var showDrawerMenu by rememberSaveable { mutableStateOf(false) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                showDrawerMenu = when(navBackStackEntry?.destination?.route) {
                    // TODO: hide routes
                    else -> true
                }

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
                            topBar = {
                                if (showDrawerMenu) {
                                    TopAppBar(
                                        title = {},
                                        navigationIcon = {
                                            IconButton(onClick = {
                                                coroutineScope.launch {
                                                    if (drawerState.isOpen) {
                                                        drawerState.close()
                                                    } else {
                                                        drawerState.open()
                                                    }
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Menu,
                                                    contentDescription = "MenuButton"
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        ) {
                            MainNavGraph(
                                modifier = Modifier.padding(it),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopDaniManagementTheme {
        Greeting("Android")
    }
}