package com.rfcreations.inventorymanager.ui.screens.edititemscreen

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.repository.cartrepository.CartRepository
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val cartRepository: CartRepository,
    private val context: Application
) : ViewModel() {

    private val _item = MutableStateFlow<InventoryItemEntity?>(null)
    val item = _item.asStateFlow()

    fun getItemDetail(itemId: String) {
        _item.value = inventoryRepository.getItemById(itemId)
    }

    fun updateCart(item: CartEntity) {
        viewModelScope.launch {
             cartRepository.updateCartItem(item)
        }
    }

    private val _savingItemProgressBar = MutableStateFlow(false)
    val savingItemProgressBar = _savingItemProgressBar.asStateFlow()

    suspend fun saveInventoryAction(isImageChanged:Boolean,item: InventoryItemEntity) {
        try {
            Log.d("imagePath", item.imagePath.toString())
            _savingItemProgressBar.value = true
            if (isImageChanged) {
                Log.d("inserting", item.imagePath.toString())
                inventoryRepository.insertItem(
                    item.copy(
                        imagePath = moveCachedImageToFileDir(
                            item.itemId,
                            item.imagePath.toString()
                        )
                    )
                )
            } else {
                Log.d("inserting else block", item.imagePath.toString())

                inventoryRepository.insertItem(item)
            }
            _savingItemProgressBar.value = false
        } catch (e: Exception){
            Log.e("Error saving",e.message.toString(),e)
            e.printStackTrace()
            withContext(Dispatchers.Main){
                Toast.makeText(context,"An error occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveCachedImageToFileDir(itemId: String, cachedImagedPath: String): String {
        val cachedImgUri = Uri.parse(cachedImagedPath)
        val inputStream = context.contentResolver.openInputStream(cachedImgUri)
        val dir = File(context.filesDir,Constants.IMG_FOLDER_NAME)
        if (!dir.exists()) dir.mkdirs()
        val imageFile = File(dir,"$itemId.jpg")
        val outputStream = FileOutputStream(imageFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
                input.close()
                clearCache()
            }
        }
        return imageFile.absolutePath
    }

    private fun clearCache() {
        val cacheDir = context.cacheDir
        cacheDir.deleteRecursively()
    }
}