package com.example.stopwatchapp.ui.screens.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stopwatchapp.R
import com.example.stopwatchapp.ui.common.BottomBar
import com.example.stopwatchapp.ui.common.TopBar
import com.example.stopwatchapp.ui.screens.home.common.HomeCommonBottomBar
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme
import kotlinx.serialization.Serializable

@Serializable
object StopWatchHome

@Composable
fun StopWatchScreen(
    modifier: Modifier = Modifier,
    currentRoute: String?,
    viewModel: StopWatchViewModel,
    onNavigateToEntry: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    StopWatchScreenContent(
        currentRoute = currentRoute,
        stopWatchTime = uiState.stopWatchTime,
        isRunning = uiState.isRunning,
        isPaused = uiState.isPaused,
        onClickForStart = viewModel::start,
        onClickForStop = viewModel::stop,
        onClickForReset = viewModel::reset,
        onNavigateToEntry = onNavigateToEntry,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopWatchScreenContent(
    modifier: Modifier = Modifier,
    currentRoute: String?,
    stopWatchTime: String = stringResource(R.string.initial_stopwatch_time),
    isRunning: Boolean = false,
    isPaused: Boolean = false,
    onClickForStart: () -> Unit = {},
    onClickForStop: () -> Unit = {},
    onClickForReset: () -> Unit = {},
    onNavigateToEntry: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.title_stopwatch_screen),
                actionText = if (isPaused) stringResource(R.string.action_save) else null,
                onClick = { onNavigateToEntry(stopWatchTime) }
            )
        },
        bottomBar = {
            if (!isRunning && !isPaused) {
                BottomBar(currentRoute = currentRoute)
            } else if(isPaused) {
                HomeCommonBottomBar(
                    text = stringResource(R.string.action_reset),
                    onClick = onClickForReset,
                    modifier = Modifier.windowInsetsPadding(BottomAppBarDefaults.windowInsets) // 画面下部の3ボタンナビゲーション領域を避けて描画するよう設定
                )
            } else {
                HomeCommonBottomBar(
                    text = stringResource(R.string.status_running),
                    modifier = Modifier.windowInsetsPadding(BottomAppBarDefaults.windowInsets) // 画面下部の3ボタンナビゲーション領域を避けて描画するよう設定
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        StopWatchBody(
            innerPadding = innerPadding,
            stopWatchTime = stopWatchTime,
            isRunning = isRunning,
            onClickForStart = onClickForStart,
            onClickForStop = onClickForStop,
            modifier = Modifier
        )
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun StopWatchScreenContentPreview() {
    StopWatchAppTheme{
        StopWatchScreenContent(currentRoute = "")
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StopWatchScreenContentPreviewForDarkMode() {
    StopWatchAppTheme{
        StopWatchScreenContent(currentRoute = "")
    }
}