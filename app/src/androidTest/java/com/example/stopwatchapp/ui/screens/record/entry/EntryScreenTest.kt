package com.example.stopwatchapp.ui.screens.record.entry

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.example.stopwatchapp.R
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class EntryScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: EntryViewModel
    val context = ApplicationProvider.getApplicationContext<Context>() // 文字列リソース取得の為のコンテキスト
    val appBarTitleRes: String = context.getString(R.string.title_entry_screen) // 日：新規作成、英：New Record
    val actionCancelRes: String = context.getString(R.string.action_cancel) // 日：キャンセル、英：Cancel
    val saveButtonRes: String = context.getString(R.string.action_save) // 日：保存、英：Save


    val testTime = "01:23.45"
    val entryRoute = "com.example.stopwatchapp.ui.screens.record.entry.RecordEntry/{time}"

    @Before
    fun setup() {
        val recordRepository: RecordRepository = mock()
        val savedStateHandle  = SavedStateHandle().apply {
            set("time", testTime)
        }
        viewModel = EntryViewModel(recordRepository, savedStateHandle)
        composeTestRule.setContent {
            StopWatchAppTheme {
                EntryScreen(viewModel = viewModel, currentRoute = entryRoute)
            }
        }
    }

    @Test // タイムが表示されていることを検証
    fun entryScreen_displayTime_onFirst() {
        composeTestRule.onNodeWithText(testTime).assertIsDisplayed()
    }

    @Test // Recordのタイトルと内容の入力フォームが表示されており、入力ができていることを検証
    fun entryScreen_displayRecordInputForm() {
        // タイトルと内容の入力欄の表示を検証
        val titleTextBox = composeTestRule.onNodeWithTag("titleTextField")
        val descriptionTextBox = composeTestRule.onNodeWithTag("descriptionTextField")
        titleTextBox.assertIsDisplayed()
        descriptionTextBox.assertIsDisplayed()

        // タイトル欄に入力された文字が表示されているか検証
        val testTitle = "ランニング"
        titleTextBox.performTextInput(testTitle)
        composeTestRule.onNodeWithText(testTitle).assertIsDisplayed()

        // 内容欄に入力された文字が表示されているか検証
        val testDescription = "公園を3周する"
        descriptionTextBox.performTextInput(testDescription)
        composeTestRule.onNodeWithText(testDescription).assertIsDisplayed()
    }

    @Test// AppBarのタイトルとキャンセルボタン、BottomBarにある保存ボタンとキャンセルボタンが表示されていることを検証
    fun entryScreen_displayAppBarAndBottomButton() {
        // AppBarのタイトル、BottomBarにある保存ボタンが表示されていることを検証
        composeTestRule.onNodeWithText(appBarTitleRes).assertIsDisplayed()
        composeTestRule.onNodeWithText(saveButtonRes).assertIsDisplayed()

        // AppBarとBottomBarにそれぞれ1つずつあるキャンセルボタンが存在しかつどちらも表示されていることを検証
        val cancelButton = composeTestRule.onAllNodesWithText(actionCancelRes)
        cancelButton[0].assertIsDisplayed()
        cancelButton[1].assertIsDisplayed()
    }
}