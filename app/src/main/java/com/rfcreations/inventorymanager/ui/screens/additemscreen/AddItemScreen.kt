package com.rfcreations.inventorymanager.ui.screens.additemscreen

import android.net.Uri
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.ui.commons.ItemEditLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

/**
 * Composable function for the Add Item screen.
 * @param popBackStack pop back to home-screen.
 * @param addItemViewModel ViewModel for adding items.
 */
@Composable
fun AddItemScreen(
    popBackStack: () -> Unit,
    addItemViewModel: AddItemViewModel = hiltViewModel()
) {

    var isItemSaved by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(isItemSaved){
        if (isItemSaved) popBackStack()
    }

    // Unique item ID
    val itemId = remember { UUID.randomUUID().toString() }

    // Mutable state for various item properties
    val productName = rememberSaveable { mutableStateOf("") }
    val costPrice = rememberSaveable { mutableStateOf("") }
    val sellingPrice = rememberSaveable { mutableStateOf("") }
    val additionalInfo = rememberSaveable { mutableStateOf("") }
    val quantity = rememberSaveable { mutableIntStateOf(1) }
    val expiryDate = rememberSaveable { mutableStateOf<Long?>(null) }

    // Mutable state for item image path and URI
    val itemImagePath = rememberSaveable { mutableStateOf<Uri?>(null) }
    val cachedImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    // State for showing progress bar while saving item
    val savingItemProgressBar = addItemViewModel.savingItemProgressBar.collectAsState().value

    // Dialog for showing progress while saving item
    if (savingItemProgressBar) {
        Dialog(
            onDismissRequest = { TODO() },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            CircularProgressIndicator()
        }
    }
    ItemEditLayout(
        appBarTitle = stringResource(R.string.add_item),
        productName = productName,
        costPrice = costPrice,
        sellingPrice = sellingPrice,
        quantity = quantity,
        additionalInfo = additionalInfo,
        expiryDate = expiryDate,
        itemImagePath = itemImagePath,
        cachedImageUri = cachedImageUri,
        navBackAction = popBackStack
    ) {
        CoroutineScope(IO).launch {
            // Saving item action
            addItemViewModel.saveInventoryAction(
                InventoryItemEntity(
                    itemId = itemId,
                    name = productName.value,
                    costPrice = costPrice.value.toIntOrNull() ?: 0,
                    sellingPrice = sellingPrice.value.toIntOrNull() ?: 0,
                    availableStock = quantity.value,
                    additionalNote = additionalInfo.value,
                    imagePath = if (itemImagePath.value != null) itemImagePath.value.toString() else null,
                    timeAdded = Calendar.getInstance().timeInMillis,
                    expiryDate = expiryDate.value
                )
            )
            isItemSaved = true
        }
    }

}

