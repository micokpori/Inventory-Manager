package com.rfcreations.inventorymanager.repository.cartrepository

import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    
    suspend fun insertItemToCart(item: CartEntity)

    suspend fun updateCartItem(item: CartEntity)

    suspend fun removeItemFromCart(item: CartEntity)

    suspend fun clearCart()

    fun getAllCartItems(): Flow<List<CartEntity>>

}