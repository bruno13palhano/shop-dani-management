package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun DeliveriesScreen(
    navigateUp: () -> Unit
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveriesContent(
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.deliveries_label)) }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun DeliveriesDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeliveriesContent(
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun DeliveriesPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeliveriesContent(
                navigateUp = {}
            )
        }
    }
}
