package com.example.stopwatchapp.ui.screens.record.detail

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.example.stopwatchapp.R
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: DetailViewModel
    val context = ApplicationProvider.getApplicationContext<Context>() // 文字列リソース取得の為のコンテキスト
    val appBarTitleRes: String = context.getString(R.string.title_detail_screen) // 日：記録詳細、英：Record Detai;
    val actionExitRes: String = context.getString(R.string.action_exit) // 日：閉じる、英：Exit
    val editButtonRes: String = context.getString(R.string.action_edit) // 日：編集、英：Edit


    val testRecordId = 1L
    private val testRecord = Record(
        id = testRecordId,
        title = "テストタイトル",
        description = "テスト内容",
        time = "12:34.56",
        recordDate = "2026/01/01 12:34:56 木曜日"
    )

    @Before
    fun setup() = runTest{
        // モック作成。getRecordStreamByIdの挙動を定義。
        val recordRepository: RecordRepository = mock()
        whenever { recordRepository.getRecordStreamById(testRecordId) }.thenReturn(flowOf( testRecord))

        //viewModelにidを渡すためのsavedStateHandle作成
        val savedStateHandle  = SavedStateHandle().apply {
            set("id", testRecordId)
        }

        viewModel = DetailViewModel(recordRepository, savedStateHandle)

        composeTestRule.setContent {
            StopWatchAppTheme {
                DetailScreen(viewModel = viewModel)
            }
        }
    }

    @Test // Recordが表示されていることを検証
    fun detailScreen_displayTime_onFirst() {
        composeTestRule.onNodeWithText(testRecord.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(testRecord.description).assertIsDisplayed()
        composeTestRule.onNodeWithText(testRecord.time).assertIsDisplayed()
        composeTestRule.onNodeWithText(testRecord.recordDate).assertIsDisplayed()
    }

    @Test// AppBarのタイトルと閉じるボタン、BottomBarにある編集ボタンと閉じるボタンが表示されていることを検証
    fun detailScreen_displayAppBarAndBottomButton() {
        // AppBarのタイトル、BottomBarにある保存ボタンが表示されていることを検証
        composeTestRule.onNodeWithText(appBarTitleRes).assertIsDisplayed()
        composeTestRule.onNodeWithText(editButtonRes).assertIsDisplayed()

        // AppBarとBottomBarにそれぞれ1つずつある閉じるボタンが存在しかつどちらも表示されていることを検証
        val cancelButton = composeTestRule.onAllNodesWithText(actionExitRes)
        cancelButton[0].assertIsDisplayed()
        cancelButton[1].assertIsDisplayed()
    }
}