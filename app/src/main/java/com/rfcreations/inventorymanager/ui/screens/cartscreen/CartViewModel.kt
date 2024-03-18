package com.rfcreations.inventorymanager.ui.screens.cartscreen

import androidx.lifecycle.ViewModel
import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import com.rfcreations.inventorymanager.database.salesdb.SoldItemsEntity
import com.rfcreations.inventorymanager.repository.cartrepository.CartRepository
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.repository.soldItemsRepository.SoldItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val soldItemsRepository: SoldItemsRepository,
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    val cartItems = cartRepository.getAllCartItems()
    suspend fun clearCart() = cartRepository.clearCart()
    suspend fun removeItemFromCart(item: CartEntity) = cartRepository.removeItemFromCart(item)

    suspend fun updateQuantityToSale(item: CartEntity){
        cartRepository.updateCartItem(item)
    }

    suspend fun onSaleButtonClick(cartItems: List<CartEntity>) {
        val timeOfTransaction = Calendar.getInstance().timeInMillis
        cartItems.forEach { item ->

            //Insert cartItem into sold items database
            soldItemsRepository.insertItem(
                SoldItemsEntity(
                    itemId = item.itemId,
                    name = item.productName,
                    costPrice = item.costPrice,
                    sellingPrice = item.sellingPrice,
                    quantitySold = item.quantityToSale,
                    imagePath = item.imagePath,
                    timeOfTransaction = timeOfTransaction
                )
            )

            val originalItem = inventoryRepository.getItemById(item.itemId)
            //Reduce the available stock in the original it6em
            inventoryRepository.insertItem(
                originalItem.copy(
                    availableStock = item.availableStock - item.quantityToSale
                )
            )
            clearCart()
        }

    }
}


