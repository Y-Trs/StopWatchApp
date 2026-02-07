package com.example.stopwatchapp.ui.screens.record

import com.example.stopwatchapp.data.local.Record

data class RecordUiState(
    val id: Long = 0,
    val time: String = "",
    val title: String = "",
    val description: String = "",
    val recordDate: String = ""
)

fun RecordUiState.toRecord(): Record {
    return Record(
        id = this.id,
        title = if (this.title != "") this.title else this.recordDate,
        description = this.description,
        time = this.time,
        recordDate = this.recordDate
    )
}