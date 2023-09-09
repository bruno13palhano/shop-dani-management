package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeliveryScreen(
    screenTitle: String,
    isEditable: Boolean,
    navigateUp: () -> Unit
) {
    DeliveryContent(
        screenTitle = screenTitle,
        isEditable = isEditable,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryContent(
    screenTitle: String,
    isEditable: Boolean,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}
