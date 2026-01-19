package com.example.stopwatchapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.stopwatchapp.ui.navigation.StopWatchNavigation
import com.example.stopwatchapp.ui.screens.home.StopWatch

@Composable
fun StopWatchApp(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()
    StopWatchNavigation(navController)
}