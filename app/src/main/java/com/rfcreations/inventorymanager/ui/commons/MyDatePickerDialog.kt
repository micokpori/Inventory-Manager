package com.rfcreations.inventorymanager.ui.commons

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(dismissRequest: () -> Unit, confirm: (Long) -> Unit) {
    val datePickerState =
        rememberDatePickerState(
            initialDisplayedMonthMillis = Calendar.getInstance().timeInMillis
        )
    DatePickerDialog(
        onDismissRequest = dismissRequest,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { confirm(it) }
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {

            TextButton(onClick = dismissRequest) {
                Text("Cancel")
            }
        })
    {
        DatePicker(state = datePickerState)
    }
}