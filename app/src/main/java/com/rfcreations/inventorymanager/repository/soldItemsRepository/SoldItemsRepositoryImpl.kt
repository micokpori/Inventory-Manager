package com.rfcreations.inventorymanager.repository.soldItemsRepository

import com.rfcreations.inventorymanager.database.salesdb.SalesDao
import com.rfcreations.inventorymanager.database.salesdb.SoldItemsEntity
import kotlinx.coroutines.flow.Flow

class SoldItemsRepositoryImpl(private val salesDao: SalesDao) : SoldItemsRepository {
    override suspend fun insertItem(item: SoldItemsEntity) {
        salesDao.insertItem(item)
    }

    override suspend fun deleteItem(item: SoldItemsEntity) {
        salesDao.deleteItem(item)
    }

    override fun getAllSoldItems(): Flow<List<SoldItemsEntity>> {
        return salesDao.getAllSoldItems()
    }

    override fun getItemById(id: String): SoldItemsEntity {
        return salesDao.getItemById(id)
    }
}