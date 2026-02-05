package com.example.stopwatchapp.ui.screens.Detail

import androidx.lifecycle.SavedStateHandle
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.rule.MainDispatcherRule
import com.example.stopwatchapp.ui.screens.record.detail.DetailViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule // このRule設定でBeforeやAfterなしでDispatcherの設定や破棄が行われる
    val mainTestDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: DetailViewModel
    val testRecordId = 1L

    // テスト用のダミーデータ
    private val testRecord = Record(
        id = testRecordId,
        time = "01:23.45",
        title = "タイトル",
        description = "内容",
        recordDate = "2026/01/01"
    )

    @Before
    fun setup() {
        val recordRepository: RecordRepository = mock()
        val savedStateHandle = SavedStateHandle().apply {
            set("id", testRecordId)
        }

        // getRecordStreamByIdがよばれたらtestRecordのflowを返すように設定
        whenever { recordRepository.getRecordStreamById(testRecordId) }.thenReturn(flowOf( testRecord))

        viewModel = DetailViewModel(recordRepository, savedStateHandle)
        runTest { advanceUntilIdle() } // initのコルーチンを完了させる
    }

    @Test // viewModel初期化時のテスト。
    fun viewModel_initialize_fetchCorrectRecord() {
        val uiState = viewModel.uiState.value

        assertEquals(testRecord.id, uiState.id)
        assertEquals(testRecord.time, uiState.time)
        assertEquals(testRecord.title, uiState.title)
        assertEquals(testRecord.description, uiState.description)
        assertEquals(testRecord.recordDate, uiState.recordDate)
    }

}