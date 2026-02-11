package com.example.stopwatchapp.ui.screens.record.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.stopwatchapp.data.FakeRecordData
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.ui.common.BottomBar
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme
import kotlinx.serialization.Serializable

@Serializable
object RecordList

@Composable
fun ListScreen(
    viewModel: ListViewModel,
    currentRoute: String?,
    navigateToHome: () -> Unit = {},
    navigateToDetail: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    ListScreenContent(
        recordList = uiState.recordList,
        currentRoute = currentRoute,
        navigateToHome = navigateToHome,
        navigateToDetail = navigateToDetail
    )
}

@Composable
fun ListScreenContent(
    recordList: List<Record>,
    currentRoute: String?,
    navigateToHome: () -> Unit = {},
    navigateToDetail: (Long) -> Unit = {}
) {
    Scaffold(
        topBar = { /* 後で実装 */ },
        bottomBar = { BottomBar(currentRoute = currentRoute, onClick = navigateToHome) }
    ) {innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
                .testTag("lazyRecordColumn")
        ) {
            items(
                items = recordList,
                key = { record -> record.id }
            ){record ->
                RecordCard(
                    title = record.title,
                    recordDate = record.recordDate,
                    time = record.time,
                    navigateToDetail = { navigateToDetail(record.id) }
                )
            }
        }
    }
}

// --------------------
// 以下プレビュー
// --------------------
val previewRecordList = FakeRecordData.testRecordList

@Preview(
    showBackground = true,
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ListScreenContentPreviewForLightMode() {
    StopWatchAppTheme{
        ListScreenContent(recordList = previewRecordList, currentRoute = "")
    }
}

@Preview(
    showBackground = true,
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ListScreenContentPreviewForDarkMode() {
    StopWatchAppTheme{
        ListScreenContent(recordList = previewRecordList, currentRoute = "")
    }
}