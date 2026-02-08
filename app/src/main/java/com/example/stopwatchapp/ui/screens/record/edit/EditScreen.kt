package com.example.stopwatchapp.ui.screens.record.edit

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.stopwatchapp.R
import com.example.stopwatchapp.ui.common.TopBar
import com.example.stopwatchapp.ui.screens.record.common.Form
import com.example.stopwatchapp.ui.screens.record.common.BottomBar
import com.example.stopwatchapp.ui.screens.record.delete.DeleteDialog
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class RecordEdit(val id: Long)

@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    viewModel: EditViewModel,
    navigateToUpdatedDetail: (Long) -> Unit = {},
    navigateBack: () -> Unit = {},
    navigateToHome: () -> Unit = {},
    currentRoute: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    EditContents(
        modifier = modifier,
        stopWatchTime = uiState.time,
        recordDate = uiState.recordDate,
        title = uiState.title,
        description = uiState.description,
        onTitleChanged = viewModel::updateTitle,
        onDescriptionChanged = viewModel::updateDescription,
        update = viewModel::update,
        navigateToUpdatedDetail = navigateToUpdatedDetail,
        navigateToHome = navigateToHome,
        cancel = navigateBack,
        delete = viewModel::delete,
        currentRoute = currentRoute,
    )

}

@Composable
fun EditContents(
    modifier: Modifier = Modifier,
    stopWatchTime: String,
    recordDate: String,
    title: String,
    description: String,
    onTitleChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    update: suspend () -> Long,
    navigateToUpdatedDetail: (Long) -> Unit = {},
    navigateToHome: () -> Unit = {},
    cancel: () -> Unit = {},
    delete: suspend () -> Unit = {},
    currentRoute: String? = null
) {
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.title_edit_screen),
                actionText = stringResource(R.string.action_cancel),
                onClick = cancel
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.windowInsetsPadding(BottomAppBarDefaults.windowInsets),
                textForConfirm = stringResource(R.string.action_update),
                textForNotConfirm = stringResource(R.string.action_delete),
                isDestructive = true,
                onClickForConfirm = {
                    coroutineScope.launch {
                        val id = update()
                        navigateToUpdatedDetail(id)
                    }
                },
                onClickForNotConfirm = { showDeleteDialog = true }
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
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged,
                currentRoute = currentRoute
            )
            if (showDeleteDialog) {
                DeleteDialog(
                    onConfirm = {
                        coroutineScope.launch {
                            delete()
                            navigateToHome()
                        }
                    },
                    onDismiss = { showDeleteDialog = false }
                )
            }
        }
    }
}

// --------------------
// 以下プレビュー
// --------------------
private object EditPreviewData {
    const val STOPWATCH_TIME = "12:34.56"
    const val RECORD_DATE = "2026/01/01 12:34:56 木曜日"
    const val TITLE = "ランニング"
    const val DESCRIPTION = "近所の公園を3周"
}

@Preview(
    name = "light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun EditContentsPreviewForLightMode() {
    StopWatchAppTheme{
        EditContents(
            stopWatchTime = EditPreviewData.STOPWATCH_TIME,
            title = EditPreviewData.TITLE,
            description = EditPreviewData.DESCRIPTION,
            recordDate = EditPreviewData.RECORD_DATE,
            update = {1L}
        )
    }
}

@Preview(
    name = "dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EditContentsPreviewForDarkMode() {
    StopWatchAppTheme{
        EditContents(
            stopWatchTime = EditPreviewData.STOPWATCH_TIME,
            title = EditPreviewData.TITLE,
            description = EditPreviewData.DESCRIPTION,
            recordDate = EditPreviewData.RECORD_DATE,
            update = {1L}
        )
    }
}