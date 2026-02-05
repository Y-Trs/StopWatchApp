package com.example.stopwatchapp.ui.screens.record.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.stopwatchapp.R
import com.example.stopwatchapp.ui.common.TopBar
import com.example.stopwatchapp.ui.screens.record.common.Form
import com.example.stopwatchapp.ui.screens.record.common.BottomBar
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme
import kotlinx.serialization.Serializable

@Serializable
data class RecordDetail(val id: Long)

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel,
    onNavigateToHome: () -> Unit = {},
    onNavigateToEdit: () -> Unit = {},
    currentRoute: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    DetailContents(
        modifier = modifier,
        stopWatchTime = uiState.time,
        recordDate = uiState.recordDate,
        title = uiState.title,
        description = uiState.description,
        onNavigateToHome = onNavigateToHome,
        onNavigateToEdit = onNavigateToEdit,
        currentRoute = currentRoute
    )

}

@Composable
fun DetailContents(
    modifier: Modifier = Modifier,
    stopWatchTime: String,
    recordDate: String,
    title: String,
    description: String,
    onNavigateToHome: () -> Unit = {},
    onNavigateToEdit: () -> Unit = {},
    currentRoute: String? = null
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.title_detail_screen),
                actionText = stringResource(R.string.action_exit),
                onClick = onNavigateToHome
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.windowInsetsPadding(BottomAppBarDefaults.windowInsets),
                textForConfirm = stringResource(R.string.action_edit),
                textForNotConfirm = stringResource(R.string.action_exit),
                onClickForConfirm = onNavigateToEdit,
                onClickForNotConfirm = onNavigateToHome
            )
        }
    ) {innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ){
            Form(
                modifier = modifier,
                stopWatchTime = stopWatchTime,
                recordDate = recordDate,
                title = title,
                description = description,
                currentRoute = currentRoute
            )
        }
    }
}

// --------------------
// 以下プレビュー
// --------------------
private object DetailPreviewData {
    const val STOPWATCH_TIME = "08:12.34"
    const val RECORD_DATE = "2024/08/15 10:30:00 木曜日"
    const val TITLE = "朝のジョギング"
    const val DESCRIPTION = "公園を3周。天気も良く、気持ちよく走れた。"
}

@Preview(
    name = "lignt mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun DetailContentsPreviewForLightMode() {
    StopWatchAppTheme{
        DetailContents(
            stopWatchTime = DetailPreviewData.STOPWATCH_TIME,
            title = DetailPreviewData.TITLE,
            description = DetailPreviewData.DESCRIPTION,
            recordDate = DetailPreviewData.RECORD_DATE,
        )
    }
}

@Preview(
    name = "dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DetailContentsPreviewForDarkMode() {
    StopWatchAppTheme{
        DetailContents(
            stopWatchTime = DetailPreviewData.STOPWATCH_TIME,
            title = DetailPreviewData.TITLE,
            description = DetailPreviewData.DESCRIPTION,
            recordDate = DetailPreviewData.RECORD_DATE,
        )
    }
}