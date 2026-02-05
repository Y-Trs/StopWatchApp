package com.example.stopwatchapp.ui.navigation

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.stopwatchapp.R
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StopWatchNavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    val sleepTime = 300L // ストップウォッチをスタートさせてから停止させるまでの時間を0.3秒とする

    //    ----------------------------------------
    //    ↓　文字列リソース　↓
    //    ----------------------------------------
    val context = ApplicationProvider.getApplicationContext<Context>() // 文字列リソース取得の為のコンテキスト
    val appBarHomeTitleRes: String = context.getString(R.string.title_stopwatch_screen) // 日：ストップウォッチ、英：StopWatch
    val appBarEntryTitleRes: String = context.getString(R.string.title_entry_screen) // 日：新規作成、英：New Record
    val appBarDetailTitleRes: String = context.getString(R.string.title_detail_screen) // 日：記録詳細、英：Record Detail
    val initialStopWatchTimeText: String = context.getString(R.string.initial_stopwatch_time) // 00:00.00
    val  saveButton: String = context.getString(R.string.action_save)// 日：保存、英：New Save


    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            StopWatchNavigation(navController = navController)
        }
    }

    //    ----------------------------------------
    //    ↓　テストで使用する画面遷移を実行するメソッド　↓
    //    ----------------------------------------
    private fun navigateFromHomeToEntry() { // ストップウォッチ画面でタイム測定し、新規作成画面へ移動する処理
        composeTestRule.onNodeWithText(initialStopWatchTimeText).performClick()
        Thread.sleep(sleepTime) // スタートからストップまでの待機時間
        composeTestRule.onNodeWithTag("stopwatch_screen_test_column").performClick()
        composeTestRule.onNodeWithText(saveButton).performClick()
    }

    private fun navigateFromEntryToDetail() = runTest{ // タイム測定→の新規作成画面から記録を保存しRecordの詳細へ移動する処理
        navigateFromHomeToEntry()
        composeTestRule.onNodeWithText(saveButton).performClick()
    }

    //    ----------------------------------------
    //    ↓　テスト　↓
    //    ----------------------------------------
    @Test
    fun navigation_verifyStartDestination() { // デフォルトでストップウォッチ画面が表示されていることを検証
        composeTestRule.onNodeWithText(appBarHomeTitleRes).isDisplayed()
        composeTestRule.onNodeWithText(initialStopWatchTimeText).isDisplayed()
        val expectedRoute = "com.example.stopwatchapp.ui.screens.home.StopWatchHome"
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(expectedRoute, currentRoute)
    }

    @Test
    fun navigation_clickSaveOnHome_navigateToEntry() {
        navigateFromHomeToEntry()
        val expectedRoute = "com.example.stopwatchapp.ui.screens.record.entry.RecordEntry/{time}"
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(expectedRoute, currentRoute)
    }

    @Test
    fun navigation_clickSaveOnEntry_navigateToDetail() {
        navigateFromEntryToDetail()

        // DetailScreenのAppBarタイトルが表示されることの検証と、表示されるまで待つことで、画面遷移の完了を保証する
        composeTestRule.onNodeWithText(appBarDetailTitleRes).assertIsDisplayed()

        val expectedRoute = "com.example.stopwatchapp.ui.screens.record.detail.RecordDetail/{id}"
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(expectedRoute, currentRoute)
    }
}