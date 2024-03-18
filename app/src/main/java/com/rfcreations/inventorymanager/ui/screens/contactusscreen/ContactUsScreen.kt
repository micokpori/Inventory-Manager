package com.rfcreations.inventorymanager.ui.screens.contactusscreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.ui.commons.GenericAppBar
import com.rfcreations.inventorymanager.utils.urlOpener

@Composable
fun ContactUsScreen(navBackAction: () -> Unit) {
    Scaffold(topBar = {
        GenericAppBar(
            title = stringResource(id = R.string.contact_us),
            navBackIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navBackAction = navBackAction
        )
    }) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValue)
        ) {
            SocialMediaColumn(
                modifier = Modifier.fillMaxWidth(),
                socialMediaName = stringResource(R.string.telegram),
                R.drawable.telegram_icon,
                urlToOpen = stringResource(R.string.telegram_contact_url)
            )
            SocialMediaColumn(
                modifier = Modifier.fillMaxWidth(),
                socialMediaName = stringResource(R.string.whatsapp),
                R.drawable.whatsapp_icon,
                urlToOpen = stringResource(R.string.whatsapp_contact_url)
            )
        }
    }
}

@Composable
private fun SocialMediaColumn(
    modifier: Modifier = Modifier,
    socialMediaName: String,
    @DrawableRes socialMediaIcon: Int,
    urlToOpen: String
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .clickable {
                urlOpener(context, urlToOpen)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = socialMediaIcon), contentDescription = null)
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = socialMediaName, style = MaterialTheme.typography.titleLarge)
    }
}