package com.example.stopwatchapp.repository

import com.example.stopwatchapp.data.local.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun insertRecord(record: Record)
    suspend fun updateRecord(record: Record)
    suspend fun deleteRecord(record: Record)
    fun getRecordStreamById(id: Long): Flow<Record?>
    fun getAllRecordStream(): Flow<List<Record>>
    fun getSearchedRecordStream(searchWord: String): Flow<List<Record>>
}