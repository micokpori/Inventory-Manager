package com.rfcreations.inventorymanager.utils

import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.rfcreations.inventorymanager.MainActivity
import java.io.File


//This Code Block has not been used for this project and will likely remain so
object ImageUtils {
    private const val FILE_PROVIDER_AUTHORITY = "your.package.name.provider"
    private const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA

    fun takePicture(activity: MainActivity, onImageCaptured: (Uri) -> Unit) {
        val context = activity.applicationContext
        val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)

        val takePictureLauncher = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { isTaken ->
            if (isTaken) {
                onImageCaptured(uri)
            }
        }

        if (ContextCompat.checkSelfPermission(context, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            takePictureLauncher.launch(uri)
        } else {
            // Request camera permission
            val permissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    takePictureLauncher.launch(uri)
                } else {
                    // Handle permission denied
                    // You may show a message or take appropriate action
                }
            }
            permissionLauncher.launch(CAMERA_PERMISSION)
        }
    }
}
