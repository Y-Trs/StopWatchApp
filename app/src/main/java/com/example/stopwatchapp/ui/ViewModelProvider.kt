package com.example.stopwatchapp.ui

import android.app.Application
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stopwatchapp.data.local.RecordDao
import com.example.stopwatchapp.data.local.RecordDatabase
import com.example.stopwatchapp.repository.RecordRepositoryForRoom
import com.example.stopwatchapp.ui.screens.home.StopWatchViewModel
import com.example.stopwatchapp.ui.screens.home.SystemTimeSource
import com.example.stopwatchapp.ui.screens.record.detail.DetailViewModel
import com.example.stopwatchapp.ui.screens.record.edit.EditViewModel
import com.example.stopwatchapp.ui.screens.record.entry.EntryViewModel
import com.example.stopwatchapp.ui.screens.record.list.ListViewModel

// 各ViewModelの生成を一元管理するファイル
object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer { // ストップウォッチ画面用
            StopWatchViewModel(timeSource = SystemTimeSource)
        }
        initializer { // 新規登録用
            val recordDao = getRecordDao(this)
            EntryViewModel(
                recordRepository = RecordRepositoryForRoom(recordDao),
                savedStateHandle = createSavedStateHandle()
            )
        }
        initializer { // 詳細画面用
            val recordDao = getRecordDao(this)
            DetailViewModel(
                recordRepository = RecordRepositoryForRoom(recordDao),
                savedStateHandle = createSavedStateHandle()
            )
        }
        initializer { // 編集画面用
            val recordDao = getRecordDao(this)
            EditViewModel(
                recordRepository = RecordRepositoryForRoom(recordDao),
                savedStateHandle = createSavedStateHandle()
            )
        }
        initializer { // 一覧画面用
            val recordDao = getRecordDao(this)
            ListViewModel(recordRepository = RecordRepositoryForRoom(recordDao))
        }
    }

    private fun getRecordDao(creationExtras: CreationExtras) : RecordDao{
        val application = (creationExtras[APPLICATION_KEY] as Application)
        return RecordDatabase.getDatabase(application).recordDao()
    }
}