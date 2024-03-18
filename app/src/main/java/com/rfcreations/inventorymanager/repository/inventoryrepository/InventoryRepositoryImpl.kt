package com.rfcreations.inventorymanager.repository.inventoryrepository

import com.rfcreations.inventorymanager.database.inventorydb.InventoryDao
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

class InventoryRepositoryImpl(private val inventoryDao: InventoryDao) : InventoryRepository {

    override suspend fun insertItem(item: InventoryItemEntity) {
        inventoryDao.insertInventoryItem(item)
    }

    override suspend fun deleteItem(item: InventoryItemEntity) {
        inventoryDao.deleteItem(item)
    }

    override fun getAllInventoryItems(): Flow<List<InventoryItemEntity>> {
        return inventoryDao.getAllInventoryItems()
    }

    override fun getItemById(id: String): InventoryItemEntity {
        return inventoryDao.getItemById(id)
    }
}