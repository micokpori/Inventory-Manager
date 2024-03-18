package com.rfcreations.inventorymanager.repository.cartrepository

import com.rfcreations.inventorymanager.database.cartdb.CartDao
import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartDao: CartDao) : CartRepository {

    override suspend fun insertItemToCart(item: CartEntity) {
        cartDao.insertItemToCart(item)
    }

    override suspend fun updateCartItem(item: CartEntity) {
        cartDao.updateCartItem(item)
    }

    override suspend fun removeItemFromCart(item: CartEntity) {
        cartDao.removeItemFromCart(item)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override fun getAllCartItems(): Flow<List<CartEntity>> {
        return cartDao.getAllCartItems()
    }
}