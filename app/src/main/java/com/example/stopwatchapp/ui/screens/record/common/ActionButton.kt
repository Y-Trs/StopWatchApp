package com.example.stopwatchapp.ui.screens.record.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    isConfirm: Boolean = false, // 保尊、編集、更新ボタンを想定
    isDestructive: Boolean = false, // 削除ボタンを想定
    onClick: () -> Unit = {}
) {
    if (isConfirm || isDestructive) {
        Button(
            modifier = modifier,
            colors =
                if (isConfirm) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                },
            onClick = onClick
        ) {
            Text(text = text)
        }
    } else { // キャンセル、閉じる、戻るボタンを想定
        OutlinedButton(
            modifier = modifier,
            colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
            onClick = onClick
        ) {
            Text(text = text)
        }
    }
}

@Preview(
    name = "light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ActionButtonPreviewForLightMode() {
    StopWatchAppTheme{
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ActionButton(text = "保存", isConfirm = true)
            ActionButton(text = "閉じる", isConfirm = false)
        }
    }
}

@Preview(
    name = "dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ActionButtonPreviewForDarkMode() {
    StopWatchAppTheme{
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ActionButton(text = "保存", isConfirm = true)
            ActionButton(text = "閉じる", isConfirm = false)
        }
    }
}