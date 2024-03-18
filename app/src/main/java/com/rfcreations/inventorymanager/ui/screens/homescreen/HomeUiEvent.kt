package com.rfcreations.inventorymanager.ui.screens.homescreen

import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity

sealed class HomeUiEvent{
    data class DeleteItem(val item: InventoryItemEntity): HomeUiEvent()
    data class UpdateSortMethod(val newValue:Int): HomeUiEvent()
    data class AddItemToCart(val item:CartEntity): HomeUiEvent()
    data class RemoveItemFromCart(val item: CartEntity): HomeUiEvent()
}
