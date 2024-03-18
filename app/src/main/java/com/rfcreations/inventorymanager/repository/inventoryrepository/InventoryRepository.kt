package com.rfcreations.inventorymanager.repository.inventoryrepository

import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {

    suspend fun insertItem(item: InventoryItemEntity)

    suspend fun deleteItem(item: InventoryItemEntity)

    fun getAllInventoryItems(): Flow<List<InventoryItemEntity>>

    fun getItemById(id : String): InventoryItemEntity
}