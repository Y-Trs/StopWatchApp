package com.example.stopwatchapp.ui.screens

import com.example.stopwatchapp.data.FakeRecordData
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.rule.MainDispatcherRule
import com.example.stopwatchapp.ui.screens.record.list.ListViewModel
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ListViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()
    private lateinit var viewModel: ListViewModel
    private val recordRepository: RecordRepository = mock()

    private val testRecordList: List<Record> = FakeRecordData.testRecordList

    @Before
    fun setup() {
        viewModel = ListViewModel(recordRepository)
        whenever { recordRepository.getAllRecordStream() }.thenReturn(flowOf(testRecordList))
    }

    @Test // viewModel初期化時に全てのレコードを取得しているかテスト
    fun viewModel_getAllRecord_atFirst() {
        val recordList = viewModel.uiState.value.recordList
        recordList.forEachIndexed { i, record ->
            assert(record == testRecordList[i])
        }
    }
}