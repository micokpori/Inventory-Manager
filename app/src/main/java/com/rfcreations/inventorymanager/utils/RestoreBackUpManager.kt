package com.rfcreations.inventorymanager.utils

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.database.salesdb.SoldItemsEntity
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.repository.soldItemsRepository.SoldItemsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import kotlin.math.pow

class RestoreBackUpManager @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val soldItemsRepository: SoldItemsRepository,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth,
    private val context: Application
) {

    enum class RestoreState {
        Idle,
        Commencing,
        DownloadingInventory,
        SavingInventoryDataSuccess,
        DownloadingSalesData,
        SavingSalesDataSuccess,
        DownloadingImages,
        SavingImagesSuccess,
        Error
    }

    private val _restoreState = MutableStateFlow(RestoreState.Idle)
    val restoreState = _restoreState.asStateFlow()

    private val backUpPath =
        firebaseStorage.reference.child(firebaseAuth.uid!!)

    private val maxDownloadSize = 1024.00.pow(50).toLong() // fifty(50) MegaByte

    suspend fun commenceRestore() {
        if (firebaseAuth.uid != null) {
            try {

                _restoreState.value = RestoreState.Commencing //starting backup
                Log.d("uid", firebaseAuth.uid!!)

                delay(1000)
                val ref = firebaseStorage.reference.child(firebaseAuth.uid!!)
                Log.d("backups", ref.listAll().toString())

                //Restore inventory
                _restoreState.value = RestoreState.DownloadingInventory
                val inventoryData =
                    ref.child(Constants.INVENTORY_FILE_NAME).getBytes(maxDownloadSize).await()
                Log.d("inventory result", inventoryData.decodeToString())
                val inventoryDataJson =
                    Json.decodeFromString<List<InventoryItemEntity>>(inventoryData.decodeToString())
                inventoryDataJson.forEach { item ->
                    inventoryRepository.insertItem(item)
                }

                //Restore sold items
                _restoreState.value = RestoreState.DownloadingSalesData
                val salesData =
                    ref.child(Constants.SOLD_ITEMS_FILE_NAME).getBytes(maxDownloadSize).await()
                Log.d("sales result", salesData.decodeToString())
                val salesDataJson =
                    Json.decodeFromString<List<SoldItemsEntity>>(salesData.decodeToString())
                salesDataJson.forEach { item ->
                    soldItemsRepository.insertItem(item)
                }

                //Restoring images
                _restoreState.value = RestoreState.DownloadingImages
                val imageList = ref.child(Constants.IMG_FOLDER_NAME).listAll().await()
                Log.d("Fetched imageList", imageList.items.joinToString())

                if (imageList.items.isNotEmpty()) {
                    val imageDir = File(context.filesDir, Constants.IMG_FOLDER_NAME)
                    if (!imageDir.exists()) imageDir.mkdirs()
                    imageList.items.forEach { image ->
                        withContext(IO) {
                            val file = File(imageDir, image.name)
                            if (!file.exists()) {
                                file.createNewFile()
                                delay(3000)
                                val task = image.getFile(file).await()
                                if (task.task.isSuccessful) {
                                    Log.d("Saved image", image.name)
                                }
                            } else {
                                Log.d("image already exist", image.name)
                            }
                        }
                    }
                    delay(500)
                    _restoreState.value = RestoreState.SavingImagesSuccess
                }
            } catch (e: Exception) {
                _restoreState.value = RestoreState.Error
            }
        } else {
            withContext(Main) {
                _restoreState.value = RestoreState.Error
                Toast.makeText(
                    context,
                    "Please re-login to perform this action",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun cancelRestore() {
        try {
            runBlocking {
                backUpPath.child("${Constants.IMG_FOLDER_NAME}/").activeDownloadTasks.forEach { it.cancel() }
                backUpPath.child(Constants.INVENTORY_FILE_NAME).activeDownloadTasks.forEach { it.cancel() }
                backUpPath.child(Constants.SOLD_ITEMS_FILE_NAME).activeDownloadTasks.forEach { it.cancel() }
            }
        } catch (e: Exception) {
            Log.e("Error on cancel backup", "E: ${e.message}")
        }
    }
}