package com.example.biolab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat

// A data class to hold our calculation results nicely
data class DilutionResult(
    val primaryName: String,
    val primaryStockVol: Double,
    val primaryBufferVol: Double,
    val secondaryName: String,
    val secondaryStockVol: Double,
    val secondaryBufferVol: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntibodyScreen(onBack: () -> Unit) {
    // 1. STATE MANAGEMENT
    // We use mutableStateOf so Compose knows to redraw the screen when these change.
    // We store inputs as Strings to prevent UI glitches while typing.
    var primaryAntibody by remember { mutableStateOf("Mouse anti-GAPDH") }
    var primaryDilutionStr by remember { mutableStateOf("1000") }

    var secondaryAntibody by remember { mutableStateOf("Goat anti-Mouse 488") }
    var secondaryDilutionStr by remember { mutableStateOf("1000") }

    var finalVolumeStr by remember { mutableStateOf("1000") }

    // This state holds the result. If null, we don't show the result card.
    var calculationResult by remember { mutableStateOf<DilutionResult?>(null) }

    // Helper to format numbers (remove huge decimal trails)
    val df = DecimalFormat("#.##")

    fun calculate() {
        // Convert strings to numbers safely
        val pDilution = primaryDilutionStr.toDoubleOrNull()
        val sDilution = secondaryDilutionStr.toDoubleOrNull()
        val fVolume = finalVolumeStr.toDoubleOrNull()

        if (pDilution != null && sDilution != null && fVolume != null && pDilution > 0 && sDilution > 0) {

            // 2. THE MATH
            // Volume of Stock = Final Volume / Dilution Factor
            val pStock = fVolume / pDilution
            val pBuffer = fVolume - pStock

            val sStock = fVolume / sDilution
            val sBuffer = fVolume - sStock

            calculationResult = DilutionResult(
                primaryName = primaryAntibody,
                primaryStockVol = pStock,
                primaryBufferVol = pBuffer,
                secondaryName = secondaryAntibody,
                secondaryStockVol = sStock,
                secondaryBufferVol = sBuffer
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Antibody Dilutions", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        // Add verticalScroll so the screen works on smaller phones or when keyboard is up
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- Primary Antibody Section ---
            Text("Primary Antibody", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            OutlinedTextField(
                value = primaryAntibody,
                onValueChange = { primaryAntibody = it },
                label = { Text("Primary Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = primaryDilutionStr,
                onValueChange = { primaryDilutionStr = it },
                label = { Text("Primary Dilution (e.g. 1000)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true
            )

            HorizontalDivider()

            // --- Secondary Antibody Section ---
            Text("Secondary Antibody", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

            OutlinedTextField(
                value = secondaryAntibody,
                onValueChange = { secondaryAntibody = it },
                label = { Text("Secondary Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = secondaryDilutionStr,
                onValueChange = { secondaryDilutionStr = it },
                label = { Text("Secondary Dilution (e.g. 1000)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true
            )

            HorizontalDivider()

            // --- Volume Section ---
            Text("Experiment Details", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = finalVolumeStr,
                onValueChange = { finalVolumeStr = it },
                label = { Text("Final Volume per tube (μL)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                singleLine = true
            )

            Button(
                onClick = { calculate() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Calculate Recipes")
            }

            // --- Results Display ---
            // using 'let' allows us to safely unwrap the nullable calculationResult
            calculationResult?.let { res ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tube 1: Primary", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text("• ${df.format(res.primaryStockVol)} μL of ${res.primaryName}")
                        Text("• ${df.format(res.primaryBufferVol)} μL of Buffer")

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Tube 2: Secondary", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text("• ${df.format(res.secondaryStockVol)} μL of ${res.secondaryName}")
                        Text("• ${df.format(res.secondaryBufferVol)} μL of Buffer")
                    }
                }
            }
        }
    }
}