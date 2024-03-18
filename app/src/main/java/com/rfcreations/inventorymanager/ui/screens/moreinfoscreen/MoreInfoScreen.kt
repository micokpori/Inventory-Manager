package com.rfcreations.inventorymanager.ui.screens.moreinfoscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.ui.commons.GenericAppBar
import com.rfcreations.inventorymanager.ui.navigation.Screens
import com.rfcreations.inventorymanager.ui.screens.moreinfoscreen.components.AppThemeDialog
import com.rfcreations.inventorymanager.ui.screens.moreinfoscreen.components.BackupDialog
import com.rfcreations.inventorymanager.ui.screens.moreinfoscreen.components.InfoCard
import com.rfcreations.inventorymanager.ui.screens.moreinfoscreen.components.RestoreBackUpDialog


/**
 * Top level composable function representing the more-info screen of the inventory manager app.
 *
 * @param navController The NavController used for navigating between screens.
 */
@Composable
fun MoreInfoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    moreInfoViewModel: MoreInfoViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val showAppThemeDialog by moreInfoViewModel.showAppThemeDialog.collectAsState()
    val showBackUpDialog by moreInfoViewModel.showBackUpDialog.collectAsState()
    val showRestoreBackUpDialog by moreInfoViewModel.showRestoreBackUpDialog.collectAsState()
    val backUpState by moreInfoViewModel.backUpState.collectAsState()
    val restoreState by moreInfoViewModel.restoreState.collectAsState()

    if (showAppThemeDialog) {
        AppThemeDialog(themeUiState = moreInfoViewModel.themeUiState) {
            moreInfoViewModel.onEvent(MoreInfoUiEvent.ToggleShowAppThemeDialog)
        }
    }

    if (showRestoreBackUpDialog) {
        RestoreBackUpDialog(
            restoreState,
            commenceRestore = { moreInfoViewModel.commenceRestore() },
            cancelRestore = { moreInfoViewModel.cancelRestore() }
        ) {
            moreInfoViewModel.onEvent(MoreInfoUiEvent.ToggleShowRestoreBackUpDialog)
        }
    }
    if (showBackUpDialog) {
        BackupDialog(
            backUpState = backUpState,
            commenceBackUp = { moreInfoViewModel.commenceBackup() },
            cancelBackUp = { moreInfoViewModel.cancelBackup() }
        ) {
            moreInfoViewModel.onEvent(MoreInfoUiEvent.ToggleShowBackUpDialog)
        }
    }
    Scaffold(
        topBar = {
            GenericAppBar(
                title = "More",
                navBackIcon = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                navController.navigate(Screens.HomeScreen.route)
            }
        }
    ) {
        Column(
            modifier
                .padding(it)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            InventoryImage(
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
            )
            /*

            InfoCard(
                title = "Profile",
                icon = Icons.Outlined.Person,
                modifier = Modifier.fillMaxWidth()
            ) {
                navController.navigate(Screens.ProfileScreen.route)
            }
*/

            InfoCard(
                title = "Theme",
                icon = ImageVector.vectorResource(R.drawable.outline_dark_mode_24),
                modifier = Modifier.fillMaxWidth()
            ) {
                moreInfoViewModel.onEvent(MoreInfoUiEvent.ToggleShowAppThemeDialog)
            }

            InfoCard(
                title = "Backup",
                icon = ImageVector.vectorResource(R.drawable.outline_backup_24),
                modifier = Modifier.fillMaxWidth()
            ) {
                moreInfoViewModel.onEvent(MoreInfoUiEvent.ToggleShowBackUpDialog)
            }

            InfoCard(
                title = "Restore",
                icon = ImageVector.vectorResource(R.drawable.outline_restore_24),
                modifier = Modifier.fillMaxWidth()
            ) {
                moreInfoViewModel.onEvent(MoreInfoUiEvent.ToggleShowRestoreBackUpDialog)
            }

            InfoCard(
                title = "Contact us",
                icon = Icons.Outlined.Email,
                modifier = Modifier.fillMaxWidth()
            ) {
                navController.navigate(Screens.ContactUsScreen.route)
            }

            InfoCard(
                title = "Clear cache",
                icon = ImageVector.vectorResource(R.drawable.outline_memory_24),
                modifier = Modifier.fillMaxWidth()
            ) {
                moreInfoViewModel.onEvent(MoreInfoUiEvent.ClearCache)
            }
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    val firebaseAuth = FirebaseAuth.getInstance()
                    firebaseAuth.signOut()
                    navController.navigate(Screens.SignInScreen.route)
                }) {
                Text(text = "Log out")
            }
        }
    }
}

/**
 * Composable function representing the inventory image.
 *
 * @param modifier Optional modifier for the image.
 */
@Composable
private fun InventoryImage(modifier: Modifier = Modifier) {
    // Display the inventory image with cropping content scale
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.inventory_image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}