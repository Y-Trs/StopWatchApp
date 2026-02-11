package com.example.stopwatchapp.ui.screens.record.list

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme

@Composable
fun RecordCard(
    modifier: Modifier = Modifier,
    title: String,
    recordDate: String,
    time: String,
    navigateToDetail: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable(onClick = navigateToDetail ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = recordDate, fontSize = 8.sp)
                }
                Spacer(Modifier.weight(1f))
                Text(text = time, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "light mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun RecordCardPreviewForLightMode() {
    StopWatchAppTheme{
        RecordCard(
            title = "3kmランニング",
            recordDate = "2026/01/01 12:34:56 木曜日",
            time = "12:34.56"
        )
    }
}

@Preview(
    showBackground = true,
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RecordCardPreviewForDarkMode() {
    StopWatchAppTheme{
        RecordCard(
            title = "3kmランニング",
            recordDate = "2026/01/01 12:34:56 木曜日",
            time = "12:34.56"
        )
    }
}