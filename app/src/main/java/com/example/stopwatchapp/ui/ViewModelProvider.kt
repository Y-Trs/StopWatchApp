package com.example.stopwatchapp.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stopwatchapp.ui.screens.home.StopWatchViewModel
import com.example.stopwatchapp.ui.screens.home.SystemTimeSource

// 各ViewModelの生成を一元管理するファイル
object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            StopWatchViewModel(timeSource = SystemTimeSource)
        }
    }
}