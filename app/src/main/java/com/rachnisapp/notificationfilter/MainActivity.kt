package com.rachnisapp.notificationfilter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rachnisapp.notificationfilter.ui.theme.NotificationFilterTheme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "POST_NOTIFICATIONS permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "POST_NOTIFICATIONS permission denied", Toast.LENGTH_LONG)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (!isNotificationListenerEnabled(this)) {
            promptNotificationListenerPermission()
        }

        if (isAndroid13OrAbove()) {
            requestPostNotificationsPermission()
        }

        setContent {
            NotificationFilterTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            onEnableNotificationsClick = { promptNotificationListenerPermission() },
                            isPermissionGranted = isNotificationListenerEnabled(this)
                        )
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isNotificationListenerEnabled(this)) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNotificationListenerEnabled(context: Context): Boolean {
        val enabledListeners =
            Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return enabledListeners?.contains(context.packageName) == true
    }

    private fun promptNotificationListenerPermission() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }

    private fun requestPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun isAndroid13OrAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onEnableNotificationsClick: () -> Unit,
    isPermissionGranted: Boolean
) {
    var isFilterEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Notification Filter App")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onEnableNotificationsClick) {
            Text(text = if (isPermissionGranted) "Permissions Granted" else "Enable Permissions")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (isFilterEnabled) "Filter is ON" else "Filter is OFF")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isFilterEnabled, onCheckedChange = { isFilterEnabled = it })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Built by Rachni")
    }
}
