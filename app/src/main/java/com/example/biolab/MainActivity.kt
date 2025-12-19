package com.example.biolab

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import com.example.biolab.navigation.LabAppNavigation

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

