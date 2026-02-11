package com.example.stopwatchapp.ui.screens.record.list

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import com.example.stopwatchapp.R
import com.example.stopwatchapp.data.FakeRecordData
import com.example.stopwatchapp.data.local.Record
import com.example.stopwatchapp.repository.RecordRepository
import com.example.stopwatchapp.ui.theme.StopWatchAppTheme
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: ListViewModel
    val recordRepository: RecordRepository = mock()
    private val context = ApplicationProvider.getApplicationContext<Context>() // 文字列リソース取得の為のコンテキスト
    private val bottomNavBarForTimerRes: String = context.getString(R.string.bottom_nav_timer) // 日：タイム測定、英：Timer
    private val bottomNavBarForRecordsRes: String = context.getString(R.string.bottom_nav_records) // 日：記録一覧、英：Records
    private val testRecordList: List<Record> = FakeRecordData.testRecordList
    private val editRoute = RecordList::class.qualifiedName // "com.example.stopwatchapp.ui.screens.record.list.RecordList"

    @Before
    fun setup() {
        whenever{recordRepository.getAllRecordStream()}.thenReturn(flowOf(testRecordList))
        viewModel = ListViewModel(recordRepository)
        composeTestRule.setContent {
            StopWatchAppTheme {
                ListScreen(viewModel = viewModel, currentRoute = editRoute)
            }
        }
    }

    @Test // 画面呼び出し時にRecordの一覧が表示されていることを検証
    fun listScreen_displayRecord_atFirst() {
        val lazyRecordColumn = composeTestRule.onNodeWithTag("lazyRecordColumn")
        testRecordList.forEach { record ->
            lazyRecordColumn.performScrollToNode(hasText(record.title)) // 検証対象のレコードのタイトルが表示されるまでスクロールさせる
            composeTestRule.onNodeWithText(record.title).assertIsDisplayed()
        }
    }

    @Test // 画面呼び出し時にボトムバーの各画面タイトルが表示されていることを検証
    fun listScreen_displayBottomNavBarText_atFirst() {
        composeTestRule.onNodeWithText(bottomNavBarForTimerRes).assertIsDisplayed()
        composeTestRule.onNodeWithText(bottomNavBarForRecordsRes).assertIsDisplayed()
    }
}