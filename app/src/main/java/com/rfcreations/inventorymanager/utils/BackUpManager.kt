package com.rfcreations.inventorymanager.utils

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.repository.soldItemsRepository.SoldItemsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject


/**
 * BackUpManager is responsible of uploading app data to
 * firebase cloud storage
 *@param inventoryRepository repo for accessing inventory items
 * @param soldItemsRepository repo for accessing sold items
 * @param firebaseStorage firebase storage for uploading data
 * @param firebaseAuth
 * @param context
 * */
class BackUpManager @Inject constructor(
    inventoryRepository: InventoryRepository,
    soldItemsRepository: SoldItemsRepository,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth,
    private val context: Application
) {
    enum class BackUpState {
        Idle,
        Commencing,
        UploadingInventory,
        UploadingInventoryDataSuccess,
        UploadingSalesData,
        UploadingSalesDataSuccess,
        UploadingImages,
        UploadingImagesSuccess,
        Error
    }

    private val _backUpState = MutableStateFlow(BackUpState.Idle)
    val backUpState = _backUpState.asStateFlow()

    private val json = Json { prettyPrint = true }

    private val backUpPath =
        firebaseStorage.reference.child(firebaseAuth.uid!!)
    private val imageDir = File(context.filesDir, Constants.IMG_FOLDER_NAME)


    private val inventoryItems = inventoryRepository.getAllInventoryItems()
    private val soldItems = soldItemsRepository.getAllSoldItems()

    suspend fun commenceBackUp() {
        if (firebaseAuth.uid != null) {
            try {
                _backUpState.value = BackUpState.Commencing //starting backup
                delay(1000)
                    Log.d("uid", firebaseAuth.uid!!)
                    val inventoryData = inventoryItems.firstOrNull()
                    val salesData = soldItems.firstOrNull()

                    if (inventoryData != null) {
                        Log.d("Start", "inventoryItems not null")
                        _backUpState.value = BackUpState.UploadingInventory
                        delay(500)
                        Log.d("Encoding", "encoding start")
                        val data = json.encodeToString<List<InventoryItemEntity>>(inventoryData)
                            .toByteArray()
                        Log.d("Encoding", "encoding finished")
                        val storageReference = backUpPath.child(Constants.INVENTORY_FILE_NAME)
                        val task = storageReference.putBytes(data).await()
                        if (task.task.isSuccessful) {
                            _backUpState.value = BackUpState.UploadingInventoryDataSuccess
                            delay(1500)
                        } else {
                            _backUpState.value = BackUpState.Error
                        }
                    }
                    if (salesData != null) {
                        _backUpState.value = BackUpState.UploadingSalesData
                        delay(1500)

                        val data = json.encodeToString(salesData).toByteArray()
                        val task =
                            backUpPath.child(Constants.SOLD_ITEMS_FILE_NAME).putBytes(data).await()
                        if (task.task.isSuccessful) {
                            _backUpState.value = BackUpState.UploadingSalesDataSuccess
                            delay(1500)
                        } else {
                            _backUpState.value = BackUpState.Error
                        }
                    }
                    // Check if Image directory exists and is indeed a directory
                    if (imageDir.exists() && imageDir.isDirectory) {
                        _backUpState.value = BackUpState.UploadingImages
                        delay(1500)

                        val imageFiles = imageDir.listFiles()
                        // Loop through each image file and upload it to Firebase Storage
                        imageFiles?.forEach { imageFile ->
                            Log.d("backing up imageFile", imageFile.absolutePath)
                            val path =
                                backUpPath.child(Constants.IMG_FOLDER_NAME).child(imageFile.name)
                            val task = path.putBytes(imageFile.readBytes()).await()
                            // Upload the image to Firebase Storage
                            if (task.task.isSuccessful) {
                                _backUpState.value = BackUpState.UploadingImagesSuccess
                                delay(1500)
                            } else {
                                _backUpState.value = BackUpState.Error
                            }
                        }
                    } else {

                    }
            } catch (e: Exception) {
                _backUpState.value = BackUpState.Error
            }
        } else {
            _backUpState.value = BackUpState.Error
        }
    }

    fun cancelBackUp() {
        try {
            runBlocking {
                backUpPath.child("${Constants.IMG_FOLDER_NAME}/").activeUploadTasks.forEach { it.cancel() }
                backUpPath.child(Constants.INVENTORY_FILE_NAME).activeUploadTasks.forEach { it.cancel() }
                backUpPath.child(Constants.SOLD_ITEMS_FILE_NAME).activeUploadTasks.forEach { it.cancel() }
            }
        } catch (e: Exception) {
            Log.e("Error on cancel backup", "E: ${e.message}")
        }
    }
}