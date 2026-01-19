package com.example.stopwatchapp.ui.screens.home.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeCommonBottomBar(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    BottomAppBar(
        containerColor = Color.Red,
        modifier = modifier
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

            ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 32.sp
            )
        }
    }
}