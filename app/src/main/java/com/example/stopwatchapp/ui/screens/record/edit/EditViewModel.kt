package com.example.stopwatchapp.ui.screens.record.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.ui.screens.record.RecordUiState
import com.example.stopwatchapp.ui.screens.record.toRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditViewModel(
    private val recordRepository: RecordRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()
    private val id: Long = checkNotNull(savedStateHandle["id"])

    init {
        viewModelScope.launch {
            val record: Record = checkNotNull( recordRepository.getRecordStreamById(id).first() )
            _uiState.update { currentState ->
                currentState.copy(
                    id = record.id,
                    time = record.time,
                    title = record.title,
                    description = record.description,
                    recordDate = record.recordDate
                )
            }
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

    suspend fun update(): Long {
        // タイトルが空白なら記録日時をタイトルに使用
        if (_uiState.value.title == "") {
            _uiState.update { currentState ->
                currentState.copy(title = currentState.recordDate)
            }
        }

        // 更新処理
        val record: Record = _uiState.value.toRecord()
        recordRepository.updateRecord(record)
        return record.id
    }

    suspend fun delete() {
        val record: Record = _uiState.value.toRecord()
        recordRepository.deleteRecord(record)
    }
}