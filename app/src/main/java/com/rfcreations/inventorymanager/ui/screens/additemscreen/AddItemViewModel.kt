package com.rfcreations.inventorymanager.ui.screens.additemscreen

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val context: Application,
    private val inventoryRepository: InventoryRepository,
) : ViewModel() {

   private val _savingItemProgressBar = MutableStateFlow(false)
    val savingItemProgressBar = _savingItemProgressBar.asStateFlow()

    suspend fun saveInventoryAction(item: InventoryItemEntity) {
        _savingItemProgressBar.value = true
        if (item.imagePath != null)
        inventoryRepository.insertItem(
            item.copy(
                imagePath = moveCachedImageToFileDir(
                    item.itemId,
                    item.imagePath
                )
            )

        ) else inventoryRepository.insertItem(item)
        _savingItemProgressBar.value = false
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