package com.example.stopwatchapp.ui.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    actionText: String? = null,
    onClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        actions = {
            if (actionText != null) {
                TextButton(onClick = onClick) {
                    Text(actionText)
                }
            }
        }
    )
}