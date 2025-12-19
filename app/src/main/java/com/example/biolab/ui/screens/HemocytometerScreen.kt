package com.example.biolab.ui.screens

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biolab.ui.components.NiceConcentrationText

// --- Tool 1: Hemocytometer ---
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HemocytometerScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    // Using a list of 2 for two independent counting chambers/squares
    var counts by remember { mutableStateOf(listOf(0, 0)) }
    var dilution by remember { mutableStateOf("1") }

    val total = counts.sum()

// Only count squares that have been touched (count > 0)
    val squaresCounted = counts.count { it > 0 }.coerceAtLeast(1)
    val avg = total.toDouble() / squaresCounted

    val concentration = avg * (dilution.toDoubleOrNull() ?: 1.0) * 10000

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = { Text("Hemocytometer", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } },
                actions = { IconButton(onClick = { counts = listOf(0, 0) }) { Icon(Icons.Default.Refresh, "Reset") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Stats Board
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF262626))) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total Cells", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("$total", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Avg/Square", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("%.1f".format(avg), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                    }
                }
            }

            // Concentration Readout
            Box(modifier = Modifier.padding(vertical = 40.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                NiceConcentrationText(concentration)
            }

            // Counting Grid (Two Large Independent Buttons)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                counts.forEachIndexed { index, count ->
                    Button(
                        onClick = {
                            val newList = counts.toMutableList()
                            newList[index]++
                            counts = newList
                            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                        },
                        modifier = Modifier.weight(1f).height(140.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Count ${index + 1}", fontSize = 14.sp)
                            Text("$count", fontSize = 40.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = dilution,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) dilution = it },
                label = { Text("Dilution Factor") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                shape = RoundedCornerShape(16.dp)
            )

            Text(
                "Formula: (Avg per 1mm² square) × Dilution × 10⁴",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}