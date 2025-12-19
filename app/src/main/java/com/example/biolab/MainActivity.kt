package com.example.biolab

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.biolab.ui.screens.AntibodyScreen
import com.example.biolab.ui.screens.DilutionScreen
import com.example.biolab.ui.screens.HemocytometerScreen
import com.example.biolab.ui.screens.HomeScreen

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

