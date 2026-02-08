package com.example.stopwatchapp.ui.screens.record.edit

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.example.stopwatchapp.R
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class EditScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: EditViewModel
    val context = ApplicationProvider.getApplicationContext<Context>() // 文字列リソース取得の為のコンテキスト
    val appBarTitleRes: String = context.getString(R.string.title_edit_screen) // 日：編集画面、英：Record Edit
    val actionCancelRes: String = context.getString(R.string.action_cancel) // 日：キャンセル、英：Cancel
    val updateButtonRes: String = context.getString(R.string.action_update) // 日：更新、英：Update
    val deleteButtonRes: String = context.getString(R.string.action_delete) // 日：削除、英：Delete
    // 日：選択した記録を削除してもよろしいでしょうか？、英：Are you sure you want to delete the record(s)？
    val dialogDescriptionRes: String = context.getString(R.string.deleteDialog_description)


    val testRecordId = 1L
    private val testRecord = Record(
        id = testRecordId,
        title = "テストタイトル",
        description = "テスト内容",
        time = "12:34.56",
        recordDate = "2026/01/01 12:34:56 木曜日"
    )
    val editRoute = "com.example.stopwatchapp.ui.screens.record.edit.RecordEdit/{id}"

    @Before
    fun setup() {
        val recordRepository: RecordRepository = mock()
        whenever{recordRepository.getRecordStreamById(testRecordId)}.thenReturn(flowOf(testRecord))
        val savedStateHandle  = SavedStateHandle().apply {
            set("id", testRecordId)
        }
        viewModel = EditViewModel(recordRepository, savedStateHandle)
        composeTestRule.setContent {
            StopWatchAppTheme {
                EditScreen(viewModel = viewModel, currentRoute = editRoute)
            }
        }
    }

    @Test // Recordが表示されていることを検証
    fun editScreen_displayRecord_onFirst() {
        composeTestRule.onNodeWithText(testRecord.time).assertIsDisplayed()
        composeTestRule.onNodeWithText(testRecord.recordDate).assertIsDisplayed()
        composeTestRule.onNodeWithText(testRecord.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(testRecord.description).assertIsDisplayed()
    }

    @Test // Recordのタイトルと内容の入力フォームが表示されており、入力ができていることを検証
    fun editScreen_displayRecordInputForm() {
        // タイトルと内容の入力欄の表示を検証
        val titleTextBox = composeTestRule.onNodeWithTag("titleTextField")
        val descriptionTextBox = composeTestRule.onNodeWithTag("descriptionTextField")
        titleTextBox.assertIsDisplayed()
        descriptionTextBox.assertIsDisplayed()

        // タイトル欄に入力された文字が表示されているか検証
        val testTitle = "ランニング"
        titleTextBox.performTextClearance() // 一度、現在の入力内容をクリアしないと文字列が結合される為
        titleTextBox.performTextInput(testTitle)
        composeTestRule.onNodeWithText(testTitle).assertIsDisplayed()

        // 内容欄に入力された文字が表示されているか検証
        val testDescription = "公園を3周する"
        descriptionTextBox.performTextClearance()// 一度、現在の入力内容をクリアしないと文字列が結合される為
        descriptionTextBox.performTextInput(testDescription)
        composeTestRule.onNodeWithText(testDescription).assertIsDisplayed()
    }

    @Test// AppBarのタイトルとキャンセルボタン、BottomBarにある更新ボタンが表示されていることを検証
    fun editScreen_displayAppBarAndBottomButton() {
        composeTestRule.onNodeWithText(appBarTitleRes).assertIsDisplayed()
        composeTestRule.onNodeWithText(updateButtonRes).assertIsDisplayed()
        composeTestRule.onNodeWithText(actionCancelRes).assertIsDisplayed()
    }

    @Test // 削除ボタンを押すと削除ダイアログが表示されていることを検証
    fun editScreen_clickDeleteButton_showDeleteDialog() {
        // 削除ボタンが表示されていることの検証とクリック
        composeTestRule.onNodeWithText(deleteButtonRes)
            .assertIsDisplayed()
            .performClick()

        // 削除ダイアログが表示されているかを検証。ここでは説明が表示されていることを検証。
        composeTestRule.onNodeWithText(dialogDescriptionRes).assertIsDisplayed()
    }
}