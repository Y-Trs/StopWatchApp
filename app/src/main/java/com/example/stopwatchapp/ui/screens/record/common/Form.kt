package com.example.stopwatchapp.ui.screens.record.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatchapp.R
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme

@Composable
fun Form(
    modifier: Modifier = Modifier,
    stopWatchTime: String,
    recordDate: String = "",
    title: String = "",
    description: String = "",
    onTitleChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    currentRoute: String? = null,
) {
    val readOnly: Boolean = when {
        currentRoute?.contains("RecordEntry") == true -> false
        currentRoute?.contains("RecordEdit") == true -> false
        else -> true
    }
    val recordTitleRes = stringResource(R.string.record_title)
    val recordDescriptionRes = stringResource(R.string.record_description)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column( // タイムと記録日時のカラム
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stopWatchTime,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = recordDate,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
        Column( // タイトルと内容のカラム
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.testTag("titleTextField"),
                value = title,
                label = { Text(recordTitleRes) },
                singleLine = true,
                onValueChange = { onTitleChanged(it) },
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                modifier = Modifier.testTag("descriptionTextField"),
                value = description,
                label = { Text(recordDescriptionRes) },
                onValueChange = { onDescriptionChanged(it) },
                readOnly = readOnly,
                maxLines = 10 // 10行まで表示
            )
        }
    }
}

@Preview(
    name = "light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun FormPreviewForLightMode() {
    StopWatchAppTheme{
        Form(stopWatchTime = "00:00.00", recordDate = "2026/01/01 13:00:00 木曜日")
    }
}

@Preview(
    name = "dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FormPreviewForDarkMode() {
    StopWatchAppTheme{
        Form(stopWatchTime = "00:00.00", recordDate = "2026/01/01 13:00:00 木曜日")
    }
}