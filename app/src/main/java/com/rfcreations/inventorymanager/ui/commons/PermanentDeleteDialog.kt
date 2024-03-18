package com.rfcreations.inventorymanager.ui.commons

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rfcreations.inventorymanager.R

@Composable
fun PermanentDeleteDialog(
    toggleDialogAction: () -> Unit,
    deleteAction: () -> Unit
) {
    AlertDialog(
        onDismissRequest = toggleDialogAction,
        confirmButton = {
                OutlinedButton(
                    onClick = { deleteAction();toggleDialogAction() }
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
        },
        dismissButton = {
            TextButton(onClick = toggleDialogAction) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.delete_title))
        },
        text = {
            Text(stringResource(id = R.string.permanent_delete_content))
        }
    )
}