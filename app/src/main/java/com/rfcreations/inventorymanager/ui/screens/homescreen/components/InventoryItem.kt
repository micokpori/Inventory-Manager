package com.rfcreations.inventorymanager.ui.screens.homescreen.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.ui.commons.ImageCard
import com.rfcreations.inventorymanager.ui.commons.NoImageCard
import com.rfcreations.inventorymanager.ui.commons.OutOfStockCard
import com.rfcreations.inventorymanager.ui.commons.PermanentDeleteDialog
import com.rfcreations.inventorymanager.utils.DateConverter

@Composable
fun InventoryItem(
    item: InventoryItemEntity,
    addToCartAction: () -> Unit,
    removeItemFromCart: () -> Unit,
    navToEditItemScreen: () -> Unit,
    deleteItemAction: () -> Unit,
    isExpiryTabSelected:Boolean,
    isItemInCart: Boolean,
    modifier: Modifier = Modifier
) {

    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    if (showDeleteDialog) {
        PermanentDeleteDialog(
            toggleDialogAction = { showDeleteDialog = !showDeleteDialog },
            deleteAction = deleteItemAction
        )
    }

    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { navToEditItemScreen() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Log.d("imagePathhh", item.imagePath.toString())

            if (item.imagePath == null) {
                NoImageCard(
                    R.drawable.no_stock_img,
                    modifier
                        .size(100.dp)
                        .weight(0.3f)
                )
            } else {
                ImageCard(
                    imagePath = Uri.parse(item.imagePath),
                    modifier
                        .padding(8.dp)
                        .size(100.dp)
                        .weight(0.3f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            Column(
                modifier
                    .weight(0.7f)
                    .align(Alignment.Top)
            ) {
                Text(text = item.name, maxLines = 3, overflow = TextOverflow.Ellipsis)
                Text(text = "₦${item.sellingPrice}", fontWeight = FontWeight.Bold)
                Row {
                    Icon(painterResource(id = R.drawable.store_outlined), null)
                    Text(text = item.availableStock.toString())
                }
                if (isExpiryTabSelected){
                    val expiryDate = item.expiryDate?.let { DateConverter.convertLongToDate(it,"dd MMM YYY") }
                    Text(text = "Expiry date: $expiryDate", fontWeight = FontWeight.Bold)
                }

            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                onClick = { showDeleteDialog = true }
            ) {
                Text(text = "Delete")
            }
            if (item.availableStock == 0) {
                OutOfStockCard()
            }
            else {
                CustomButton(
                    isItemInCart,
                    addToCartAction,
                    removeItemFromCart
                )
            }
        }
    }
}

@Composable
private fun CustomButton(
    isItemInCart: Boolean,
    addToCartAction: () -> Unit,
    removeItemFromCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                if (isItemInCart) removeItemFromCart() else addToCartAction()
            },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = if (isItemInCart) "Remove from cart" else "Add to cart",
            modifier = Modifier.padding(12.dp)
        )
    }
}

