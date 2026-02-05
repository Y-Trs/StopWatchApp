package com.example.stopwatchapp.ui.screens.record.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.ui.screens.record.RecordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val recordRepository: RecordRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()
    // nullの場合IllegalStateExceptionをスローさせクラッシュさせる。Fail-Firstの考え方。
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
}