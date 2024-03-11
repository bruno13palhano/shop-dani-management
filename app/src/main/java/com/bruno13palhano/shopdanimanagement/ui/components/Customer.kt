package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class)
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
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(16.dp),
                onClick = onPhotoClick,
                shape = RoundedCornerShape(5)
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.customer_photo_label),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5)),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.customer_photo_label),
                        contentScale = ContentScale.Crop
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