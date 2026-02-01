package com.jbass.orbital.presentation.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jbass.orbital.ui.theme.OrbitalTheme

@Composable
fun ManualConnectionDialog(
    onConnect: (String) -> Unit
) {
    var ip by remember { mutableStateOf("192.168.1.") }

    AlertDialog(
        onDismissRequest = {}, // Force user to enter IP
        title = { Text("Server Not Found") },
        text = {
            Column {
                Text("Could not find Orbital Server automatically. Please enter the IP address.")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("Server IP") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConnect(ip) }) {
                Text("Connect")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun previewManualConnection(){
    OrbitalTheme() {
        ManualConnectionDialog {  }
    }
}