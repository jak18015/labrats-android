package com.example.biolab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntibodyScreen(onBack: () -> Unit) {
    var primaryAntibody: String = "Mouse anti-GAPDH"
    var primaryAntibodyDilution: Int = 1000
    var secondaryAntibody: String = "Goat anti-Mouse 488"
    var secondaryAntibodyDilution: Int = 1000
    var finalVolume: Double = 1000.0

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Antibody Dilutions", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Primary Antibody", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = primaryAntibody,
                    onValueChange = { primaryAntibody = it },
                    label = { Text("Name") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
            Text("Dilution", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = primaryAntibodyDilution.toString(),
                    onValueChange = { primaryAntibodyDilution = it.toIntOrNull() ?: 0 },
                    label = { Text("Dilution") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Text("Secondary Antibody", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = secondaryAntibody,
                    onValueChange = { secondaryAntibody = it },
                    label = { Text("Name") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
            Text("Dilution", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = secondaryAntibodyDilution.toString(),
                    onValueChange = { secondaryAntibodyDilution = it.toIntOrNull() ?: 0 },
                    label = { Text("Dilution") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Text("Final Volume", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = finalVolume.toString(), onValueChange = { finalVolume = it.toDoubleOrNull() ?: 0.0 }, label = { Text("Final Volume") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
            }
        }
    }