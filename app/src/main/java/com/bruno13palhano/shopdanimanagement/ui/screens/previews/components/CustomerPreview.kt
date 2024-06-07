package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CustomerContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomerDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = 0L, label = "") {
                    CustomerContent(
                        screenTitle = stringResource(id = R.string.new_customer_label),
                        snackbarHostState = remember { SnackbarHostState() },
                        name = "",
                        photo = byteArrayOf(),
                        email = "",
                        address = "",
                        city = "",
                        phoneNumber = "",
                        gender = "",
                        age = "",
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this,
                        onNameChange = {},
                        onEmailChange = {},
                        onAddressChange = {},
                        onCityChange = {},
                        onPhoneNumberChange = {},
                        onGenderChange = {},
                        onAgeChange = {},
                        onPhotoClick = {},
                        onOutsideClick = {},
                        onDoneButtonClick = { it },
                        navigateUp = {}
                    )
                }
            }
        }
    }
}