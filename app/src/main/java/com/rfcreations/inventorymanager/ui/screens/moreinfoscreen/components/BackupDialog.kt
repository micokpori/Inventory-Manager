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
import com.rfcreations.inventorymanager.utils.BackUpManager

@Composable
fun BackupDialog(
    backUpState: BackUpManager.BackUpState,
    commenceBackUp: () -> Unit,
    cancelBackUp: () -> Unit,
    dismissRequest: () -> Unit) {

    var isCancelButtonEnabled by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(true){
        commenceBackUp()
    }

    AlertDialog(
        onDismissRequest = dismissRequest,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            TextButton(
                enabled = isCancelButtonEnabled,
                onClick = {
                    isCancelButtonEnabled = false
                    cancelBackUp()
                    dismissRequest()
                }
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Backing Up...")
        },
        text = {
            Column {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                when (backUpState) {
                    BackUpManager.BackUpState.Idle -> {

                    }
                    BackUpManager.BackUpState.Commencing -> {
                        Text(text = "Commencing BackUp")
                    }
                    BackUpManager.BackUpState.UploadingInventory -> {
                        Text(text = "Uploading Inventory items")
                    }
                    BackUpManager.BackUpState.UploadingInventoryDataSuccess -> {
                        Text(text = "Uploaded Inventory items Successfully")
                    }
                    BackUpManager.BackUpState.UploadingSalesData -> {
                        Text(text = "Uploading Sold items")
                    }
                    BackUpManager.BackUpState.UploadingSalesDataSuccess -> {
                        Text(text = "Uploaded Sold items Successfully")
                    }
                    BackUpManager.BackUpState.UploadingImages -> {
                        Text(text = "Uploading Images")
                    }
                    BackUpManager.BackUpState.UploadingImagesSuccess -> {
                        Text(text = "Uploaded Images Successfully")
                       Toast.makeText(context,"BackUp has concluded",Toast.LENGTH_SHORT).show()
                        cancelBackUp()
                        dismissRequest()
                    }
                    BackUpManager.BackUpState.Error -> {
                        cancelBackUp()
                        dismissRequest()
                        Toast.makeText(context,"An error occurred during backUp",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )
}