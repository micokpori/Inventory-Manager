package com.rfcreations.inventorymanager.ui.screens.homescreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import com.rfcreations.inventorymanager.R

@Composable
fun InventoryHomeAppBar(
    placeHolder: String,
    searchQuery: MutableState<String>,
    modifier: Modifier = Modifier,
    cartSize: Int,
    navToCartScreen: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(verticalAlignment = Alignment.CenterVertically) {

        OutlinedTextField(
            modifier = modifier.weight(0.8f),
            value = searchQuery.value,
            onValueChange = { if (it.length < 10) searchQuery.value = it },
            leadingIcon = { Icon(Icons.Filled.Search, null) },
            trailingIcon = {
                if (searchQuery.value.isNotEmpty()) {
                    IconButton(onClick = { searchQuery.value = ""; keyboardController?.hide() }) {
                        Icon(Icons.Filled.Clear, null)
                    }
                }
            },
            singleLine = true,
            placeholder = {
                Text(text = placeHolder)
            },
            shape = shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        //Display total number of items in cart
        BadgedBox(
            modifier = modifier.weight(0.2f),
            badge = { Badge { Text(text = "$cartSize") } }) {
            IconButton(
                onClick = navToCartScreen
            ) {
                Icon(painterResource(id = R.drawable.cart), null)
            }
        }
    }
}