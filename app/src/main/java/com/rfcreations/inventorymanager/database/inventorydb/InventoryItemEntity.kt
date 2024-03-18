package com.rfcreations.inventorymanager.database.inventorydb

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
@Entity(tableName = "inventory_items")
data class InventoryItemEntity(
    @PrimaryKey val itemId: String,
    val name: String,
    val costPrice: Int,
    val sellingPrice: Int,
    val availableStock: Int,
    val additionalNote: String,
    val imagePath: String?,
    val timeAdded: Long,
    val lastModified: Long = Calendar.getInstance().timeInMillis,
    val expiryDate: Long?
)