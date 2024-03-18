package com.rfcreations.inventorymanager.ui.screens.moreinfoscreen.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import com.rfcreations.inventorymanager.utils.RestoreBackUpManager

@Composable
fun RestoreBackUpDialog(
    restoreState: RestoreBackUpManager.RestoreState,
    commenceRestore: () -> Unit,
    cancelRestore: () -> Unit,
    dismissRequest: () -> Unit
) {
    var isCancelButtonEnabled by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(true) {
        commenceRestore()
    }

    AlertDialog(
        onDismissRequest = dismissRequest,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            TextButton(
                enabled = isCancelButtonEnabled,
                onClick = {
                    isCancelButtonEnabled = false
                    cancelRestore()
                    dismissRequest()
                }
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Restoring...")
        },
        text = {
            Column {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                when (restoreState) {
                    RestoreBackUpManager.RestoreState.Idle -> {

                    }

                    RestoreBackUpManager.RestoreState.Commencing -> {
                        Text(text = "Commencing Restoration")
                    }

                    RestoreBackUpManager.RestoreState.DownloadingInventory -> {
                        Text(text = "Downloading Inventory items")
                    }

                    RestoreBackUpManager.RestoreState.SavingInventoryDataSuccess -> {
                        Text(text = "Restored Inventory items Successfully")
                    }

                    RestoreBackUpManager.RestoreState.DownloadingSalesData -> {
                        Text(text = "Downloading Sold items")
                    }

                    RestoreBackUpManager.RestoreState.SavingSalesDataSuccess -> {
                        Text(text = "Restored Sold items Successfully")
                    }

                    RestoreBackUpManager.RestoreState.DownloadingImages -> {
                        Text(text = "Downloading Images")
                    }

                    RestoreBackUpManager.RestoreState.SavingImagesSuccess -> {
                        Text(text = "Images Restored Successfully")
                        Toast.makeText(context, "Restoration completed successfully", Toast.LENGTH_SHORT)
                            .show()
                        cancelRestore()
                        dismissRequest()
                    }

                    RestoreBackUpManager.RestoreState.Error -> {
                        cancelRestore()
                        dismissRequest()
                    }
                }
            }
        }
    )
}