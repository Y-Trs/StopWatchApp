package com.example.stopwatchapp.ui.screens.record.delete

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.stopwatchapp.R
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme

@Composable
fun DeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    // 文字列リソース
    val dialogTitleText = stringResource(R.string.deleteDialog_title)
    val dialogTitleDescription = stringResource(R.string.deleteDialog_description)
    val deleteButtonText = stringResource(R.string.action_delete)
    val cancelButtonText = stringResource(R.string.action_cancel)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = dialogTitleText) },
        text = { Text(text = dialogTitleDescription) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(deleteButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(cancelButtonText)
            }
        }
    )
}

@Preview(
    showBackground = true,
    name = "light mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun DeleteDialogPreviewForLightMode() {
    StopWatchAppTheme{
        DeleteDialog(onConfirm = {}, onDismiss = {})
    }
}

@Preview(
    showBackground = true,
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DeleteDialogPreviewForDarkMode() {
    StopWatchAppTheme{
        DeleteDialog(onConfirm = {}, onDismiss = {})
    }
}