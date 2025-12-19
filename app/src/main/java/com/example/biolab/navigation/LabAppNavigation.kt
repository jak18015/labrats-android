package com.example.biolab.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.biolab.ui.screens.AntibodyScreen
import com.example.biolab.ui.screens.DilutionScreen
import com.example.biolab.ui.screens.HemocytometerScreen
import com.example.biolab.ui.screens.HomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LabAppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("hemocytometer") { HemocytometerScreen { navController.popBackStack() } }
        composable("dilution") { DilutionScreen { navController.popBackStack() } }
        composable("antibody") { AntibodyScreen { navController.popBackStack() } }
    }
}