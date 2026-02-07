package com.example.stopwatchapp.ui.screens.edit

import androidx.lifecycle.SavedStateHandle
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.rule.MainDispatcherRule
import com.example.stopwatchapp.ui.screens.record.detail.DetailViewModel
import com.example.stopwatchapp.ui.screens.record.edit.EditViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class EditViewModelTest {

    @get:Rule // このRule設定でBeforeやAfterなしでDispatcherの設定や破棄が行われる
    val mainTestDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: EditViewModel
    private val recordRepository: RecordRepository = mock()
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
        val savedStateHandle = SavedStateHandle().apply {
            set("id", testRecordId)
        }
        // getRecordStreamByIdがよばれたらtestRecordのflowを返すように設定
        whenever { recordRepository.getRecordStreamById(testRecordId) }.thenReturn(flowOf( testRecord))

        viewModel = EditViewModel(recordRepository, savedStateHandle)
        runTest { advanceUntilIdle() } // initのコルーチンを完了させる
    }

    @Test //viewModel初期化時のテスト。
    fun viewModel_initialize_hasTimeAndRecordDate(){
        val id = viewModel.uiState.value.id
        val time = viewModel.uiState.value.time
        val title = viewModel.uiState.value.title
        val description = viewModel.uiState.value.description
        val recordDate = viewModel.uiState.value.recordDate

        assertEquals(testRecord.id, id)
        assertEquals(testRecord.time, time)
        assertEquals(testRecord.title, title)
        assertEquals(testRecord.description, description)
        assertEquals(testRecord.recordDate, recordDate)
    }

    @Test // タイトルを更新した時の挙動をテスト。上限の15文字のときと、それを超える16文字でそれぞれテスト
    fun viewModel_updateTitle_hasValidAndCorrectTitle() {
        // 15文字以下の文字列でタイトルが更新されていることをテスト
        val expectedTitle = "a".repeat(15)
        viewModel.updateTitle(expectedTitle)
        var actualTitle = viewModel.uiState.value.title
        assertEquals(expectedTitle, actualTitle)

        // 15文字超でタイトルが更新されていないことをテスト
        val titleLargerThan15 = "a".repeat(16) // 上限の15文字を超える文字列
        viewModel.updateTitle(titleLargerThan15)
        actualTitle = viewModel.uiState.value.title
        assertNotEquals(titleLargerThan15, actualTitle)
    }

    @Test // 内容を更新した時の挙動をテスト。上限の100文字のときと、それを超える101文字でそれぞれテスト
    fun viewModel_updateDescription_hasValidAndCorrectDescription() {
        // 100文字以下の文字列で内容が更新されていることをテスト
        val expectedDescription = "a".repeat(100)
        viewModel.updateDescription(expectedDescription)
        var actualDescription = viewModel.uiState.value.description
        assertEquals(expectedDescription, actualDescription)

        // 100文字超で内容が更新されていないことをテスト
        val descriptionLargerThan15 = "a".repeat(101) // 上限の15文字を超える文字列
        viewModel.updateDescription(descriptionLargerThan15)
        actualDescription = viewModel.uiState.value.description
        assertNotEquals(descriptionLargerThan15, actualDescription)
    }

    @Test // タイトルが空白の場合、記録日時がタイトルに挿入され更新されるかをテスト
    fun viewModel_update_callsRepository_withBlankTitle() = runTest{
        viewModel.updateTitle("")
        viewModel.update()
        val title = viewModel.uiState.value.title
        val recordDate = viewModel.uiState.value.recordDate
        assertEquals(recordDate, title) // タイトル空白の場合、recordDateと同じになるかテスト
        verify(recordRepository).updateRecord(any()) // updateRecordメソッドが呼ばれているかテスト
    }

    @Test // タイトルが空白でない場合はそのまま更新されていることをテスト
    fun viewModel_update_callsRepository_withTitle() = runTest{
        viewModel.update()
        val title = viewModel.uiState.value.title
        val recordDate = viewModel.uiState.value.recordDate
        assertNotEquals(recordDate, title) // タイトル空白の場合、recordDateと同じになるかテスト
        verify(recordRepository).updateRecord(any()) // updateRecordメソッドが呼ばれているかテスト
    }
}