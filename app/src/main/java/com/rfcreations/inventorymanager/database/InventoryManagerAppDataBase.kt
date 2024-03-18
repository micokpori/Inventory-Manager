package com.rfcreations.inventorymanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rfcreations.inventorymanager.database.inventorydb.InventoryDao
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.database.salesdb.SalesDao
import com.rfcreations.inventorymanager.database.salesdb.SoldItemsEntity

@Database(
    entities = [InventoryItemEntity::class, SoldItemsEntity::class],
    version = 2,
    exportSchema = false
)
abstract class InventoryManagerAppDataBase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
    abstract fun salesDao(): SalesDao
}