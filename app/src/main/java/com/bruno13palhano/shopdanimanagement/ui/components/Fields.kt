package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (text: String) -> Unit,
    icon: ImageVector,
    label: String,
    placeholder: String,
    singleLine: Boolean = true,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss(),
        value = text,
        onValueChange = onTextChange,
        leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
        label = { Text(text = label, fontStyle = FontStyle.Italic) },
        placeholder = { Text(text = placeholder, fontStyle = FontStyle.Italic) },
        singleLine = singleLine,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
    )
}

@Composable
fun CustomIntegerField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (text: String) -> Unit,
    icon: ImageVector,
    label: String,
    placeholder: String,
    singleLine: Boolean = true,
    readOnly: Boolean = false
) {
    val patternInt = remember { Regex("^\\d*") }

    OutlinedTextField(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss(),
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.matches(patternInt)) {
                onValueChange(newValue)
            }
        },
        leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
        label = { Text(text = label, fontStyle = FontStyle.Italic) },
        placeholder = { Text(text = placeholder, fontStyle = FontStyle.Italic) },
        singleLine = singleLine,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = {
            defaultKeyboardAction(ImeAction.Done)
        })
    )
}

@Composable
fun CustomClickField(
    modifier: Modifier = Modifier,
    value: String,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    placeholder: String,
    singleLine: Boolean = true,
    readOnly: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .fillMaxWidth()
            .onFocusChanged { focusState -> if (focusState.hasFocus) { onClick() } },
        value = value,
        onValueChange = {},
        leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
        label = { Text(text = label, fontStyle = FontStyle.Italic) },
        placeholder = { Text(text = placeholder, fontStyle = FontStyle.Italic) },
        singleLine = singleLine,
        readOnly = readOnly
    )
}