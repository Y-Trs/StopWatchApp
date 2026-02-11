package com.example.stopwatchapp.ui.screens.record.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatchapp.repository.RecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import  com.example.stopwatchapp.data.local.Record
import kotlinx.coroutines.flow.update

data class ListRecord(
    val recordList: List<Record> = listOf()
)


class ListViewModel(private val recordRepository: RecordRepository): ViewModel(){
    private val _uiState = MutableStateFlow(ListRecord())
    val uiState: StateFlow<ListRecord> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
           val recordList: List<Record> = recordRepository.getAllRecordStream().first()
            _uiState.update { currentState ->
                currentState.copy(recordList = recordList)
            }
        }
    }
}