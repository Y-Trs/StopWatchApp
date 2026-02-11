package com.example.stopwatchapp.ui.common

import android.icu.text.CaseMap
import androidx.compose.material.icons.Icons
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.stopwatchapp.R
import com.example.stopwatchapp.ui.screens.home.StopWatchHome
import com.example.stopwatchapp.ui.screens.record.list.RecordList

data class BottomNavItem(
    val currentRoute: String?,
    val titleRes: Int,
    val iconResId: Int
)
val navItem = listOf(
    BottomNavItem(// タイム測定に関する情報
        StopWatchHome::class.qualifiedName, // "com.example.stopwatchapp.ui.screens.home.StopWatchHome"
        R.string.bottom_nav_timer,
        R.drawable.ic_timer
    ),
    BottomNavItem(// 記録一覧に関する情報
        RecordList::class.qualifiedName, // "com.example.stopwatchapp.ui.screens.record.list.RecordList"
        R.string.bottom_nav_records,
        R.drawable.ic_article
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    currentRoute: String?,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        navItem.forEachIndexed { index, navItem ->
            NavigationBarItem(
                label = { Text(text = stringResource(id = navItem.titleRes) ) },
                selected = currentRoute == navItem.currentRoute,
                icon = {
                    Icon(
                        painter = painterResource(id = navItem.iconResId),
                        contentDescription = stringResource(id = navItem.titleRes)
                    )
                },
                onClick = if (currentRoute != navItem.currentRoute) { onClick } else { {} }
            )
        }
    }
}