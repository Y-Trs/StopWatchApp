package com.example.stopwatchapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.stopwatchapp.ui.ViewModelProvider
import com.example.stopwatchapp.ui.screens.home.StopWatchHome
import com.example.stopwatchapp.ui.screens.home.StopWatchScreen
import com.example.stopwatchapp.ui.screens.record.detail.RecordDetail
import com.example.stopwatchapp.ui.screens.record.detail.DetailScreen
import com.example.stopwatchapp.ui.screens.record.entry.RecordEntry
import com.example.stopwatchapp.ui.screens.record.entry.EntryScreen

@Composable
fun StopWatchNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route

    NavHost(navController, startDestination = StopWatchHome) {
        // ストップウォッチアプリ画面
        composable<StopWatchHome> {
            StopWatchScreen(
                currentRoute = currentRoute,
                viewModel = viewModel(factory = ViewModelProvider.Factory),
                onNavigateToEntry = {time ->
                    navController.navigate(route = RecordEntry(time = time))
                }
            )
        }
        // 記録登録画面
        composable<RecordEntry> {backStackEntry ->
            val route: RecordEntry = backStackEntry.toRoute()
            EntryScreen(
                viewModel = viewModel(factory = ViewModelProvider.Factory),
                currentRoute = currentRoute,
                navigateBack = { navController.navigateUp() },
                navigateToDetail = {id ->
                    navController.navigate(RecordDetail(id = id))
                }
            )
        }
        // 記録詳細画面
        composable<RecordDetail> { backStackEntry ->
            DetailScreen(
                viewModel = viewModel(factory = ViewModelProvider.Factory),
                currentRoute = currentRoute,
                onNavigateToHome = {
                    navController.navigate(StopWatchHome) {
                        popUpTo(StopWatchHome) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToEdit = {/* 後で実装 */}
            )
        }
    }
}