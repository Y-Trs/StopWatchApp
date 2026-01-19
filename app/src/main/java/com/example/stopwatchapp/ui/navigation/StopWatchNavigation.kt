package com.example.stopwatchapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stopwatchapp.ui.ViewModelProvider
import com.example.stopwatchapp.ui.screens.home.StopWatchHome
import com.example.stopwatchapp.ui.screens.home.StopWatchScreen

@Composable
fun StopWatchNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route

    NavHost(navController, startDestination = StopWatchHome) {
        // ストップウォッチアプリ画面
        composable<StopWatchHome> {
            StopWatchScreen(
                currentRoute = currentRoute,
                viewModel = viewModel(factory = ViewModelProvider.Factory)
            )
        }
    }
}