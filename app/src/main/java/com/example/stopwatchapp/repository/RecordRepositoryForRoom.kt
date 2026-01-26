package com.example.stopwatchapp.repository

import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.data.local.RecordDao
import kotlinx.coroutines.flow.Flow

class RecordRepositoryForRoom(private val recordDao: RecordDao): RecordRepository {
    override suspend fun insertRecord(record: Record): Long = recordDao.insert(record)
    override suspend fun updateRecord(record: Record) = recordDao.update(record)
    override suspend fun deleteRecord(record: Record) = recordDao.delete(record)
    override fun getRecordStreamById(id: Long): Flow<Record?> = recordDao.getRecordById(id)
    override fun getAllRecordStream(): Flow<List<Record>> = recordDao.getRecords()
    override fun getSearchedRecordStream(searchWord: String): Flow<List<Record>> = recordDao.getRecordsBySearchWord(searchWord)
}