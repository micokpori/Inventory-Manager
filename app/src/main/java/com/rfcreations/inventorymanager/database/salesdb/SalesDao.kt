package com.rfcreations.inventorymanager.database.salesdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SoldItemsEntity)

    @Delete
    suspend fun deleteItem(item: SoldItemsEntity)

    @Query("SELECT * FROM sold_items")
    fun getAllSoldItems(): Flow<List<SoldItemsEntity>>

    @Query("SELECT * FROM sold_items WHERE itemId=:id")
    fun getItemById(id : String): SoldItemsEntity
}