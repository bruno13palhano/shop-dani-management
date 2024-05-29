package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleInputDialog(
    dialogTitle: String,
    label: String,
    placeholder: String,
    input: String,
    onInputChange: (newCategory: String) -> Unit,
    onOkClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.clip(RoundedCornerShape(10))
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier =
                        Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                    text = dialogTitle,
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    modifier =
                        Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                    value = input,
                    onValueChange = onInputChange,
                    label = { Text(text = label) },
                    placeholder = { Text(text = placeholder) }
                )
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Button(
                        modifier = Modifier.padding(end = 4.dp),
                        onClick = {
                            onDismissRequest()
                            onOkClick()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.ok_label))
                    }
                    Button(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = onDismissRequest
                    ) {
                        Text(text = stringResource(id = R.string.cancel_label))
                    }
                }
            }
        }
    }
}