package com.rfcreations.inventorymanager.ui.commons

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.utils.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "ItemCompose"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ItemEditLayout(
    appBarTitle: String,
    productName: MutableState<String>,
    costPrice: MutableState<String>,
    sellingPrice: MutableState<String>,
    quantity: MutableState<Int>,
    additionalInfo: MutableState<String>,
    expiryDate: MutableState<Long?>,
    itemImagePath: MutableState<Uri?>,
    cachedImageUri: MutableState<Uri?>,
    navBackAction: () -> Unit,
    saveItemAction: () -> Unit,
) {

    val context = LocalContext.current

    // Scroll behavior for the top app bar
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    // Coroutine scope
    val scope = rememberCoroutineScope()

    // Snackbar host state for showing snackbar messages
    val snackBarHostState = remember { SnackbarHostState() }

    // Permission for accessing camera
    val cameraPermission = android.Manifest.permission.CAMERA
    val permissionState = rememberPermissionState(cameraPermission)

    // Activity result launcher for taking picture
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isTaken ->
        if (isTaken) {
            scope.launch {
                // Launch in a background thread
                withContext(Dispatchers.IO) {
                    itemImagePath.value = Uri.parse(cachedImageUri.value.toString())
                    Log.d("path", itemImagePath.value!!.toString())
                }
            }
        } else {
            cachedImageUri.value = null
        }
    }

    // Activity result launcher for requesting permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            scope.launch {
                // Launch in a background thread
                withContext(Dispatchers.IO) {
                    val file =
                        File(context.cacheDir, System.currentTimeMillis().toString())
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                    cachedImageUri.value = uri
                    takePictureLauncher.launch(uri)
                }
            }
        }
    }

    // Scaffold for layout structure
    Scaffold(
        topBar = {
            // Custom AppBar composable
            ItemAppBar(
                appBarTitle,
                scrollBehavior,
                navBackAction = navBackAction,
                saveItemAction = saveItemAction
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        // Main content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                // Displaying image card if image path is not null
                if (itemImagePath.value != null) {
                    ImageCard(itemImagePath.value!!,
                        modifier = Modifier
                            //  .fillMaxWidth()
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                val file =
                                    File(
                                        context.cacheDir,
                                        System
                                            .currentTimeMillis()
                                            .toString()
                                    )
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    file
                                )
                                cachedImageUri.value = uri
                                takePictureLauncher.launch(uri)
                            }
                    )
                } else {
                    // Displaying no image card if image path is null
                    // This means that no image has been taken
                    NoImageCard(
                        R.drawable.camera_icon,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                when (permissionState.status) {
                                    is PermissionStatus.Granted -> {
                                        val file =
                                            File(
                                                context.cacheDir,
                                                System
                                                    .currentTimeMillis()
                                                    .toString()
                                            )
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            file
                                        )
                                        cachedImageUri.value = uri
                                        takePictureLauncher.launch(uri)
                                    }

                                    is PermissionStatus.Denied -> {
                                        if (permissionState.status.shouldShowRationale) {
                                            // Showing rationale if permission denied
                                            scope.launch {
                                                val result = snackBarHostState.showSnackbar(
                                                    message = "Permission required",
                                                    actionLabel = "Go to settings"
                                                )

                                                if (result == SnackbarResult.ActionPerformed) {
                                                    val intent = Intent(
                                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                        Uri.fromParts(
                                                            "package",
                                                            context.packageName,
                                                            null
                                                        )
                                                    )
                                                    context.startActivity(intent)
                                                }
                                            }
                                        } else {
                                            // Launching permission request if rationale not shown
                                            permissionLauncher.launch(cameraPermission)
                                        }
                                    }
                                }
                            }
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                // Composable for entering item name
                ItemName(productName.value) { productName.value = it }
                Spacer(modifier = Modifier.height(18.dp))
                // Composable for entering cost price
                PriceTextField(title = "Cost price", price = costPrice.value, "eg N100") {
                    costPrice.value = it
                }
                Spacer(modifier = Modifier.height(18.dp))
                // Composable for entering selling price
                PriceTextField(title = "Selling price", price = sellingPrice.value, "eg N150") {
                    sellingPrice.value = it
                }
                Spacer(modifier = Modifier.height(18.dp))
                ExpiryDate(expiryDate, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(18.dp))
                // Composable for adjusting quantity
                Quantity(
                    quantity = quantity.value,
                    incQuantity = { quantity.value++ },
                    decQuantity = { quantity.value-- })
                Spacer(modifier = Modifier.height(18.dp))
                // Composable for additional information
                AdditionalInfo(text = additionalInfo.value) { additionalInfo.value = it }
            }
        }
    }
}

/**
 * Composable function for displaying a custom AppBar.
 *
 * @param title Title to display in the AppBar.
 * @param scrollBehavior Scroll behavior for the AppBar.
 * @param navBackAction Action for the navigation back button.
 * @param saveItemAction Action for saving the item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    navBackAction: () -> Unit,
    saveItemAction: () -> Unit
) {
    Log.d(TAG, "ItemAppBar: Creating AppBar with title: $title")
    LargeTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            // Navigation back button
            IconButton(onClick = navBackAction) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            // Save item button
            IconButton(onClick = saveItemAction) {
                Icon(Icons.Filled.Check, null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        scrollBehavior = scrollBehavior
    )
}


/**
 * Composable function for displaying the name of the item/product.
 *
 * @param productName Name of the product.
 * @param onValueChange Callback for value change.
 */
@Composable
private fun ItemName(productName: String, onValueChange: (String) -> Unit) {
    Column {
        Text(
            text = "Name of the item",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = productName,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12),
            leadingIcon = {
                Icon(painterResource(id = R.drawable.product_name_icon), null)
            },
            onValueChange = { onValueChange(it) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )
    }
}


/**
 * Composable function for displaying a price text field.
 *
 * @param title Title of the price field.
 * @param price Price value.
 * @param placeHolderText Placeholder text for the price field.
 * @param onValueChange Callback for value change.
 */
@Composable
private fun PriceTextField(
    title: String,
    price: String,
    placeHolderText: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = price,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12),
            placeholder = { Text(text = placeHolderText) },
            leadingIcon = {
                Icon(painterResource(id = R.drawable.naira_symbol), null)
            },
            onValueChange = { if (it.isDigitsOnly()) onValueChange(it) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = if (title != "Selling price") ImeAction.Next else ImeAction.Done
            )
        )
    }
}

@Composable
private fun ExpiryDate(expiryDate: MutableState<Long?>, modifier: Modifier = Modifier) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    if (showDatePicker) {
        MyDatePickerDialog(
            dismissRequest = { showDatePicker = false },
            confirm = {
                expiryDate.value = it

                showDatePicker = false
            }
        )
    }
    Column {
        Text(
            text = "Expiry date",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Card(
            onClick = { showDatePicker = true },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier.padding(vertical = 17.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_today_24),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(text =
                if (expiryDate.value == null) "Expiry date" else DateConverter.convertLongToDate(
                    expiryDate.value!!, "dd MMM YYY"))
            }
        }
    }

}

/**
 * Composable function for displaying quantity information.
 *
 * @param quantity Quantity value.
 * @param incQuantity Action to increment quantity.
 * @param decQuantity Action to decrement quantity.
 */
@Composable
private fun Quantity(quantity: Int, incQuantity: () -> Unit, decQuantity: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = "Set Quantity")

        IconButton(onClick = {
            if (quantity != 0) decQuantity()
        }) {
            Icon(painterResource(id = R.drawable.minus_sign), contentDescription = null)
        }
        Text(text = "$quantity")
        IconButton(onClick = incQuantity) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}


/**
 * Composable function for displaying additional information.
 *
 * @param text Additional information text.
 * @param onValueChange Callback for value change.
 */
@Composable
private fun AdditionalInfo(text: String, onValueChange: (String) -> Unit) {
    Column {
        Text(
            text = "Additional info",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = text,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onValueChange(it) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
