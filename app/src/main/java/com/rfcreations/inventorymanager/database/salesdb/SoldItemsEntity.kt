package com.rfcreations.inventorymanager.database.salesdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
@Entity(tableName = "sold_items")
data class SoldItemsEntity(
    @PrimaryKey val itemId: String,
    val name: String,
    val costPrice: Int,
    val sellingPrice: Int,
    val quantitySold: Int,
    val imagePath: String? = null,
    val timeOfTransaction: Long = Calendar.getInstance().timeInMillis
)
