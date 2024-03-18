package com.rfcreations.inventorymanager.ui.commons

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


/**
 * A top app bar made for generic purposes such as displaying the current screen title
 * and handling click events
 *
 * @param title Title for current screen
 * @param navBackIcon Icon to represent navigation icon
 * @param navBackAction Called when the navigation icon is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericAppBar(
    title:String,
    navBackIcon:ImageVector,
    navBackAction:()-> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(navBackAction) {
                Icon(navBackIcon, null )
            }
        }
    )
}