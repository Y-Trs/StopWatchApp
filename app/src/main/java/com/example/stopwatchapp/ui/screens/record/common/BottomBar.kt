package com.example.stopwatchapp.ui.screens.record.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    textForConfirm: String,
    textForNotConfirm: String,
    isDestructive: Boolean = false,
    onClickForConfirm: () -> Unit = {},
    onClickForNotConfirm: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface), // ボタンの外側の余白
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionButton(
            text = textForConfirm,
            isConfirm = true,
            onClick = onClickForConfirm,
            modifier = Modifier.fillMaxWidth()
        )
        ActionButton(
            text = textForNotConfirm,
            isDestructive = isDestructive,
            onClick = onClickForNotConfirm,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    name = "light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun BottomBarPreviewForLightMode() {
    StopWatchAppTheme{
        BottomBar(textForConfirm = "保存", textForNotConfirm = "キャンセル")
    }
}

@Preview(
    name = "dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BottomBarPreviewForDarkMode() {
    StopWatchAppTheme{
        BottomBar(textForConfirm = "保存", textForNotConfirm = "キャンセル")
    }
}