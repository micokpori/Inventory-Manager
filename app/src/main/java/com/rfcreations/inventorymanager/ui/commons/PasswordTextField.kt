package com.rfcreations.inventorymanager.ui.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.rfcreations.inventorymanager.R

@Composable
fun PasswordTextField(value:String,onValueChange:(String) -> Unit) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(true) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {onValueChange(it)},
        placeholder = {
            Text(text = "Password")
        },
        leadingIcon = {
            Icon(
                ImageVector.vectorResource(R.drawable.baseline_lock_24),
                null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (!isPasswordVisible) {
                        ImageVector.vectorResource(R.drawable.visibility_24)
                    } else {
                        ImageVector.vectorResource(
                            R.drawable.visibility_off_24
                        )
                    }, contentDescription = null
                )
            }
        },
        visualTransformation = if (isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(12),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )

}