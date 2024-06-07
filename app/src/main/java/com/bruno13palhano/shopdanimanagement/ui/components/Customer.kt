package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CustomerContent(
    screenTitle: String,
    snackbarHostState: SnackbarHostState,
    name: String,
    photo: ByteArray,
    email: String,
    address: String,
    city: String,
    phoneNumber: String,
    gender: String,
    age: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onNameChange: (name: String) -> Unit,
    onEmailChange: (email: String) -> Unit,
    onAddressChange: (address: String) -> Unit,
    onCityChange: (city: String) -> Unit,
    onPhoneNumberChange: (phoneNumber: String) -> Unit,
    onGenderChange: (gender: String) -> Unit,
    onAgeChange: (age: String) -> Unit,
    onPhotoClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onDoneButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
        ) {
            with(sharedTransitionScope) {
                ElevatedCard(
                    modifier = Modifier.padding(8.dp),
                    onClick = onPhotoClick,
                    shape = RoundedCornerShape(5)
                ) {
                    AsyncImage(
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(
                                        key = "customer-$email"
                                    ),
                                    animatedVisibilityScope = animatedContentScope
                                )
                                .aspectRatio(1f)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5)),
                        model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(photo)
                                .crossfade(true)
                                .placeholderMemoryCacheKey("customer-$email")
                                .memoryCacheKey("customer-$email")
                                .build(),
                        contentDescription = stringResource(id = R.string.customer_photo_label)
                    )
                }
            }
            CustomTextField(
                text = name,
                onTextChange = onNameChange,
                icon = Icons.Filled.Title,
                label = stringResource(id = R.string.name_label),
                placeholder = stringResource(id = R.string.enter_name_label)
            )
            CustomTextField(
                text = email,
                onTextChange = onEmailChange,
                icon = Icons.Filled.Email,
                label = stringResource(id = R.string.email_label),
                placeholder = stringResource(id = R.string.enter_email_label)
            )
            CustomTextField(
                text = address,
                onTextChange = onAddressChange,
                icon = Icons.Filled.LocationCity,
                label = stringResource(id = R.string.address_label),
                placeholder = stringResource(id = R.string.enter_address_label)
            )
            CustomTextField(
                text = city,
                onTextChange = onCityChange,
                icon = Icons.Filled.Place,
                label = stringResource(id = R.string.city_label),
                placeholder = stringResource(id = R.string.enter_city_label)
            )
            CustomIntegerField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                icon = Icons.Filled.Phone,
                label = stringResource(id = R.string.phone_number_label),
                placeholder = stringResource(id = R.string.enter_phone_number_label)
            )
            CustomTextField(
                text = gender,
                onTextChange = onGenderChange,
                icon = Icons.Filled.Person,
                label = stringResource(id = R.string.gender_label),
                placeholder = stringResource(id = R.string.enter_gender_label)
            )
            CustomIntegerField(
                value = age,
                onValueChange = onAgeChange,
                icon = Icons.Filled.Cake,
                label = stringResource(id = R.string.age_label),
                placeholder = stringResource(id = R.string.enter_age_label)
            )
        }
    }
}