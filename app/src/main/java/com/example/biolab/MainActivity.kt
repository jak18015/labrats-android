package com.example.biolab

import android.R.color.black
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Color(0xFF2563EB), // Lab Blue
                secondary = Color(0xFF7C3AED) // Dilution Purple
            )) {
                LabAppNavigation()
            }
        }
    }
}

// --- Navigation Logic ---

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LabAppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("hemocytometer") { HemocytometerScreen { navController.popBackStack() } }
        composable("dilution") { DilutionScreen { navController.popBackStack() } }
    }
}

// --- Home Screen ---

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

            ToolCard("Hemocytometer", "Cell counting & concentration", Icons.Default.GridView, Color(0xFF262626), Color(0xFF2563EB)) {
                navController.navigate("hemocytometer")
            }
            Spacer(modifier = Modifier.height(12.dp))
            ToolCard("Dilution Calculator", "C1V1 = C2V2 calculator", Icons.Default.Science, Color(0xFF262626), Color(0xFF7C3AED)) {
                navController.navigate("dilution")
            }
        }
    }
}

@Composable
fun ToolCard(title: String, desc: String, icon: ImageVector, bgColor: Color, iconColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(bgColor), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(28.dp))
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(desc, color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

// --- Tool 1: Hemocytometer ---

// Fancy formatted text for concentration readout
@Composable
fun NiceConcentrationText(concentration: Double) {
    val formatter = DecimalFormat("0.00E0")
    val parts = formatter.format(concentration).split("E")
    val base = parts[0]
    val exponent = parts[1].replace("+", "")

    Text(buildAnnotatedString {
        append("Concentration: $base × 10")
        withStyle(style = SpanStyle(
            baselineShift = BaselineShift.Superscript,
            fontSize = 12.sp // Slightly smaller for the exponent
        )
        ) {
            append(exponent)
        }
        append(" cells/mL")
    })
}


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

// --- Tool 2: Dilution Calculator ---

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
            Text("C1 x V1 = C2 x V2", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = FontWeight.Bold)

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