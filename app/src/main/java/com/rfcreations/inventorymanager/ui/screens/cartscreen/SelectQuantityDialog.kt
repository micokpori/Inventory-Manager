package com.rfcreations.inventorymanager.ui.screens.cartscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly

@Composable
fun SelectQuantityDialog(
    quantity: String,
    stockAvailable: Int,
    updateQuantity: (String) -> Unit,
    dismissRequest: () -> Unit,
    confirmAction: () -> Unit
) {

    val isQuantityEligible = quantity.toInt() <= stockAvailable
     
        AlertDialog(
            title = {
                Text(text = "Enter quantity to sale",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            onDismissRequest = dismissRequest,
            confirmButton = {
                TextButton(onClick = confirmAction) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = dismissRequest) {
                    Text(text = "Cancel")
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = {
                            if (it.isDigitsOnly() && it.length < 5) updateQuantity(it)
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(text = "quantity")
                        }
                    )
                    AnimatedVisibility(visible = !isQuantityEligible) {
                        Text(text = "Quantity to be sold cannot be greater than available stock")
                    }
                }
            }
        )
}