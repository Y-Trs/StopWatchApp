package com.example.stopwatchapp.ui.screens.entry

import androidx.lifecycle.SavedStateHandle
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.ui.screens.record.entry.EntryViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class EntryViewModelTest {
    private lateinit var viewModel: EntryViewModel
    private val recordRepository: RecordRepository = mock()
    private val testTime = "01:23.45"

    @Before
    fun setup() {
        val savedStateHandle = SavedStateHandle().apply {
            set("time", testTime)
        }
        viewModel = EntryViewModel(recordRepository, savedStateHandle)
    }

    @Test //viewModel初期化時のテスト。
    fun viewModel_initialize_hasTimeAndRecordDate(){
        val time = viewModel.uiState.value.time
        val recordDate = viewModel.uiState.value.recordDate

        assertEquals(testTime, time)
        assertNotEquals("", recordDate) //　初期化後は空白でないことをテスト
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

    @Test // 保存された時の挙動をテスト
    fun viewModel_insert_hasNotNoTitle_CallsRepository() = runTest{
        viewModel.insert()
        val title = viewModel.uiState.value.title
        val recordDate = viewModel.uiState.value.recordDate
        assertEquals(recordDate, title) // タイトル空白の場合、recordDateと同じになるかテスト
        verify(recordRepository).insertRecord(any()) // insertRecordメソッドが呼ばれているかテスト
    }
}