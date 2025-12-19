package com.example.biolab.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DilutionScreen(onBack: () -> Unit) {
    var c1 by remember { mutableStateOf("") }
    var v1 by remember { mutableStateOf("") }
    var c2 by remember { mutableStateOf("") }
    var v2 by remember { mutableStateOf("") }

    fun calculate() {
        val nC1 = c1.toDoubleOrNull()
        val nV1 = v1.toDoubleOrNull()
        val nC2 = c2.toDoubleOrNull()
        val nV2 = v2.toDoubleOrNull()

        if (nC1 != null && nV1 != null && nC2 != null) v2 = (nC1 * nV1 / nC2).toString()
        else if (nC2 != null && nV2 != null && nC1 != null) v1 = (nC2 * nV2 / nC1).toString()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dilution Calc", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            Text("C1 x V1 = C2 x V2", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            Text("Stock Solution (Start)", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = c1, onValueChange = { c1 = it }, label = { Text("C1") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = v1, onValueChange = { v1 = it }, label = { Text("V1") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Target Solution (Final)", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = c2, onValueChange = { c2 = it }, label = { Text("C2") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = v2, onValueChange = { v2 = it }, label = { Text("V2") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            Button(
                onClick = { calculate() },
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp).height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Calculate Missing Value")
            }
        }
    }
}