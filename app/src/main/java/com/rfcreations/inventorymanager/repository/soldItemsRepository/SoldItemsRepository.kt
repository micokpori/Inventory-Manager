package com.rfcreations.inventorymanager.repository.soldItemsRepository

import com.rfcreations.inventorymanager.database.salesdb.SoldItemsEntity
import kotlinx.coroutines.flow.Flow

interface SoldItemsRepository {

    suspend fun insertItem(item: SoldItemsEntity)

    suspend fun deleteItem(item: SoldItemsEntity)

    fun getAllSoldItems(): Flow<List<SoldItemsEntity>>

    fun getItemById(id : String): SoldItemsEntity
}