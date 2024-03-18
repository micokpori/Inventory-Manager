package com.rfcreations.inventorymanager.database.cartdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val itemId:String,
    val productName: String,
    val availableStock:Int,
    val quantityToSale: Int,
    val costPrice:Int,
    val sellingPrice:Int,
    val imagePath:String?
)
