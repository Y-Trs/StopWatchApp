package com.example.stopwatchapp.ui.screens.record.entry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.ui.screens.record.RecordUiState
import com.example.stopwatchapp.ui.screens.record.toRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EntryViewModel(
    private val recordRepository: RecordRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()
    private val time: String = savedStateHandle["time"] ?: "00:00.00"

    // viewModelインスタンス生成時に実行。タイムと現在時刻だけ最初にRecordUiStateにセットしておく
    init {
        val now = LocalDateTime.now()
        val formatPattern = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss E曜日")
        val formatedNow = formatPattern.format(now)

        _uiState.update { currentState ->
            currentState.copy(
                time = time,
                recordDate = formatedNow
            )
        }
    }

    fun updateTitle(title: String) {
        if (validateTitle(title)) {
            _uiState.update { currentState ->
                currentState.copy(title = title)
            }
        }
    }

    fun updateDescription(description: String) {
        if (validateDescription(description)){
            _uiState.update { currentState ->
                currentState.copy(description = description)
            }
        }
    }

    private fun validateTitle(title: String): Boolean {
        return title.length <= 15 // 15文字を上限とする
    }

    private fun validateDescription(description: String): Boolean {
        return description.length <= 100// 100文字を上限とする
    }

    suspend fun insert(): Long {
        // タイトルが空白なら記録日時をタイトルに使用
        if (_uiState.value.title == "") {
            _uiState.update { currentState ->
                currentState.copy(title = currentState.recordDate)
            }
        }

        // 保存処理
        val record: Record = _uiState.value.toRecord()
        return recordRepository.insertRecord(record)
    }
}