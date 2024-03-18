package com.rfcreations.inventorymanager.ui.screens.edititemscreen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.ui.commons.ItemEditLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@Composable
fun EditItemScreen(
    itemId: String,
    popBackStack: () -> Unit,
    editItemViewModel: EditItemViewModel = hiltViewModel()
) {
    val inventoryItem = editItemViewModel.item.collectAsState().value
    var isItemSaved by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        CoroutineScope(IO).launch {
            editItemViewModel.getItemDetail(itemId)
        }
    }
    LaunchedEffect(isItemSaved) {
        if (isItemSaved) popBackStack()
    }

    if (inventoryItem === null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        val productName = rememberSaveable { mutableStateOf(inventoryItem.name) }
        val costPrice =
            rememberSaveable { mutableStateOf(inventoryItem.costPrice.toString()) }
        val sellingPrice =
            rememberSaveable { mutableStateOf(inventoryItem.sellingPrice.toString()) }
        val additionalInfo =
            rememberSaveable { mutableStateOf(inventoryItem.additionalNote) }
        val quantity =
            rememberSaveable { mutableIntStateOf(inventoryItem.availableStock) }
        val expiryDate =
            rememberSaveable { mutableStateOf(inventoryItem.expiryDate) }

        // Mutable state for item image path and URI
        val itemImagePath = rememberSaveable {
            mutableStateOf<Uri?>(
                if (inventoryItem.imagePath != null) Uri.parse(inventoryItem.imagePath) else null
            )
        }
        val cachedImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
        Log.d("itemImagePath", itemImagePath.value.toString())
        Log.d("cachedImageUri", cachedImageUri.value.toString())


        // State for showing progress bar while saving item
        val savingItemProgressBar = editItemViewModel.savingItemProgressBar.collectAsState().value

        // Dialog for showing progress while saving item
        if (savingItemProgressBar) {
            Dialog(
                onDismissRequest = { /*TODO*/ },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                CircularProgressIndicator()
            }
        }
        ItemEditLayout(
            appBarTitle = "Edit item",
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
                editItemViewModel.saveInventoryAction(
                    isImageChanged = cachedImageUri.value != null,
                    item = InventoryItemEntity(
                        itemId = itemId,
                        name = productName.value,
                        costPrice = costPrice.value.toIntOrNull() ?: 0,
                        sellingPrice = sellingPrice.value.toIntOrNull() ?: 0,
                        availableStock = quantity.value,
                        additionalNote = additionalInfo.value,
                        imagePath = if (cachedImageUri.value != null) {
                            cachedImageUri.value.toString()
                        } else if (itemImagePath.value != null) {
                            itemImagePath.value.toString()
                        } else null,
                        timeAdded = inventoryItem.timeAdded,
                        expiryDate = expiryDate.value
                    )
                )

                // Pass the updated item data to CartDb
                //this will ensure that if this item is already in cart,the changes will be applied
                editItemViewModel.updateCart(
                    CartEntity(
                        itemId = itemId,
                        productName = productName.value,
                        availableStock = quantity.value,
                        quantityToSale = 1,
                        costPrice = costPrice.value.toIntOrNull() ?: 0,
                        sellingPrice = sellingPrice.value.toIntOrNull() ?: 0,
                        imagePath = inventoryItem.imagePath
                    )
                )
                isItemSaved = true
            }
        }
    }
}