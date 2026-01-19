package com.example.stopwatchapp.ui.screens.home

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.core.app.ApplicationProvider
import com.example.stopwatchapp.MainActivity
import com.example.stopwatchapp.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StopWatchScreenTest {

//    Androidのライフサイクルや非同期処理が深く関わるテスト
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: StopWatchViewModel
    val context = ApplicationProvider.getApplicationContext<Context>() // 文字列リソース取得の為のコンテキスト
    val sleepTime = 300L // ストップウォッチをスタートさせてから停止させるまでの時間を0.3秒とする

    // StopWatch画面で必要になる文字列リソース
    val appBarTitleText: String = context.getString(R.string.title_stopwatch_screen) // 日本語：ストップウォッチ、英語：StopWatch
    val runningStatusText: String = context.getString(R.string.status_running) // 日本語：計測中、英語：Running
    val actionSaveText: String = context.getString(R.string.action_save) // 日本語：保存、英語：Save
    val actionResetText: String = context.getString(R.string.action_reset) // 日本語：リセット、英語：Reset
    val bottomNavItemForTimerText: String = context.getString(R.string.bottom_nav_timer) // 日本語：タイム測定、英語：Timer
    val bottomNavItemForRecordsText: String = context.getString(R.string.bottom_nav_records) // 日本語：記録一覧、英語：Records
    val initialStopWatchTImeText: String = context.getString(R.string.initial_stopwatch_time) // 日本語・英語：00:00.00

    @Test // 開いた直後の画面をテスト
    fun defaultScreen_withInitialStopWatchTime() {
        composeTestRule.onNodeWithText(appBarTitleText).assertIsDisplayed()
        composeTestRule.onNodeWithText(initialStopWatchTImeText).assertIsDisplayed()
        composeTestRule.onNodeWithText(bottomNavItemForTimerText).assertIsDisplayed()
        composeTestRule.onNodeWithText(bottomNavItemForRecordsText).assertIsDisplayed()
    }

    @Test // 画面クリックしストップウォッチをスタートさせた時のテスト
    fun startStopWatch_withRunning_withoutInitialStopWatchTime() {
        composeTestRule.onNodeWithText(initialStopWatchTImeText).performClick()

        // 以下、実行順序に気を付ける
        // assertIsDisplayedは上記クリックからのUI変更のタイミング誤差を少しの間同期させる機能を持っている
        // このストップウォッチアプリでは画面クリックでカウントが開始され同時に「計測中」文字列も表示されるので、
        // 「計測中」が表示されたことは=初期値の00:00.0が表示されていないことを意味する
        // 一方でassertIsNotDisplayedに同期機能はないのでクリック直後、
        // タイマー起動よりも前にテストが終わりテスト失敗の可能性がある
        // ただしcreateAndroidComposeRuleの方が本番環境向きのテストの為、こちらを使用していることが前提
        composeTestRule.onNodeWithText(runningStatusText).assertIsDisplayed()
        composeTestRule.onNodeWithText(initialStopWatchTImeText).assertIsNotDisplayed()
    }

    @Test // ストップウォッチを停止させた時のテスト
    fun pauseStopWatch_withResetText_withoutInitialStopWatchTime() {
        composeTestRule.onNodeWithText(initialStopWatchTImeText).performClick()
        Thread.sleep(sleepTime) // スタートからストップまでの待機時間
        composeTestRule.onNodeWithTag("stopwatch_screen_test_column").performClick()

        // 以下テスト
        composeTestRule.onNodeWithText(actionResetText).assertIsDisplayed()
        composeTestRule.onNodeWithText(actionSaveText).assertIsDisplayed()
        composeTestRule.onNodeWithText(runningStatusText).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(initialStopWatchTImeText).assertIsNotDisplayed()
    }

    @Test // ストップウォッチを停止させた時のテスト
    fun resetStopWatch_withInitialStopWatchTime() {
        composeTestRule.onNodeWithText(initialStopWatchTImeText).performClick()
        Thread.sleep(sleepTime) // スタートからストップまでの待機時間
        composeTestRule.onNodeWithTag("stopwatch_screen_test_column").performClick()
        composeTestRule.onNodeWithText(actionResetText).performClick()

        // 以下テスト
        composeTestRule.onNodeWithText(initialStopWatchTImeText).assertIsDisplayed()
        composeTestRule.onNodeWithText(actionResetText).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(actionSaveText).assertIsNotDisplayed()
        composeTestRule.onNodeWithText(runningStatusText).assertIsNotDisplayed()
    }
}