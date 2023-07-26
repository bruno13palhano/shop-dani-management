package com.bruno13palhano.shopdanimanagement.ui.theme.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun ShoppingScreen(
    onMenuClick: () -> Unit
) {
    ShoppingContent(
        onMenuClick = onMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingContent(
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "Shopping Screen")
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ShoppingPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingScreen(
                onMenuClick = {}
            )
        }
    }
}