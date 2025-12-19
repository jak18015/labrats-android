package com.example.biolab.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.biolab.ui.components.ToolCard

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            LargeTopAppBar(
                title = { Text("BioLab", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Select a Tool", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            ToolCard(
                "Hemocytometer",
                "Cell counting & concentration",
                Icons.Default.GridView,
                Color(0xFF262626),
                Color(0xFF2563EB)
            ) {
                navController.navigate("hemocytometer")
            }
            Spacer(modifier = Modifier.height(12.dp))
            ToolCard(
                "Dilution Calculator",
                "C1V1 = C2V2 calculator",
                Icons.Default.Science,
                Color(0xFF262626),
                Color(0xFF7C3AED)
            ) {
                navController.navigate("dilution")
            }
            Spacer(modifier = Modifier.height(12.dp))
            ToolCard(
                "Antibody Dilution Calculator",
                "Calculate final volumes to dilute antibodies for experiments",
                Icons.Default.Science,
                Color(0xFF262626),
                Color(0xFF7C3AED)
            ) {
                navController.navigate("antibody")
            }
        }
    }
}