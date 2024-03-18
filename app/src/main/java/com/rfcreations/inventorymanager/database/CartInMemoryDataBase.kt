package com.rfcreations.inventorymanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rfcreations.inventorymanager.database.cartdb.CartDao
import com.rfcreations.inventorymanager.database.cartdb.CartEntity


@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class CartInMemoryDataBase: RoomDatabase() {
    abstract fun cartDao(): CartDao
}