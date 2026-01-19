package com.example.stopwatchapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.stopwatchapp.R

@Composable
fun StopWatchBody(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    stopWatchTime: String = stringResource(R.string.initial_stopwatch_time),
    isRunning: Boolean = false,
    onClickForStart: () -> Unit = {},
    onClickForStop: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .testTag("stopwatch_screen_test_column")
            .padding(innerPadding)
            .fillMaxSize()
            .clickable {
                if (!isRunning) {
                    onClickForStart()
                } else {
                    onClickForStop()
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag("stopwatch_screen_test_time"), // UIテストのデバッグ用にタグを追加
            text = stopWatchTime,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}