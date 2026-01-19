package com.example.stopwatchapp.ui.screens.home

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import java.sql.Time
import kotlin.Long

@OptIn(ExperimentalCoroutinesApi::class)
class StopWatchViewModelTest {
    private lateinit var viewModel: StopWatchViewModel
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var fakeTimeSource: FakeTimeSource
    val initialState = StopWatch( //初期状態。初期状態ののプロパティを明示的に宣言している。
        startTime = 0,
        timeAtPaused = 0,
        stopWatchTime = "00:00.00",
        isRunning = false,
        isPaused = false
    )

    class FakeTimeSource(private val dispatcher: TestDispatcher) : TimeSource {
        override fun getElapsedTime(): Long {
            return dispatcher.scheduler.currentTime
        }
    }

    private fun start(elapsedTimeBeforeStart: Long, elapsedTimeAfterStart: Long) {
        testDispatcher.scheduler.advanceTimeBy(elapsedTimeBeforeStart)
        viewModel.start()
        testDispatcher.scheduler.advanceTimeBy(elapsedTimeAfterStart) // 100ミリ秒経過させ、While内のdelay(100)を進める
        testDispatcher.scheduler.runCurrent() // 100ミリ秒経過した時点で実行されるタスク（次のdelayまで）を完全に完了させる。
    }

    @Before
    fun setup() {
        testDispatcher = StandardTestDispatcher() //テスト用のDispatcherを生成
        Dispatchers.setMain(testDispatcher) //　メインスレッドのDispatcherをテスト用のDispatcherに設定
        fakeTimeSource = FakeTimeSource(testDispatcher)
        viewModel = StopWatchViewModel(timeSource = fakeTimeSource)
    }

    @After
    fun tearDown() {
        viewModel.timerJob?.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun viewModel_initialize_withInitialState() { // viewModel生成後の初期UI状態をテスト
        val uiState = viewModel.uiState.value
        assertEquals(initialState, uiState)
    }

    @Test
    fun afterStart_stateIsUpdatedCorrectly() = runTest { // ストップウォッチをスタートさせてからの状態をテスト
        // テストに失敗する例外を発生させ本来はテストが終わるが、start()内のwhile1が終わっていないため永遠にループする。
        // そのため、try-finallyを用いることで、テスト失敗し例外が発生しても、finallyでコルーチンを終わらせることでテストが終了する。
        try{
            val elapsedTimeBeforeStart = 3000L// システム起動からの時間が3秒経過したことにする
            val elapsedTimeAfterStart = 100L // Whileループ内のdelay1回分の時間を経過させる
            start(elapsedTimeBeforeStart, elapsedTimeAfterStart)

            val uiState = viewModel.uiState.value

            val expectedState = initialState.copy(
                startTime = elapsedTimeBeforeStart,
                stopWatchTime = "00:00.10",
                isRunning = true,
            )

            assertEquals(expectedState, uiState)
        } finally {
            viewModel.timerJob?.cancel()
        }
    }

    @Test
    fun afterPause_stateIsUpdatedCorrectly() = runTest { // ストップウォッチをスタートさせ一時停止させた状態をテスト
        // テストに失敗する例外を発生させ本来はテストが終わるが、start()内のwhile1が終わっていないため永遠にループする。
        // そのため、try-finallyを用いることで、テスト失敗し例外が発生しても、finallyでコルーチンを終わらせることでテストが終了する。
        try{
            val elapsedTimeBeforeStart = 3000L// システム起動からの時間が3秒経過したことにする
            val elapsedTimeAfterStart = 100L // Whileループ内のdelay1回分の時間を経過させる
            start(elapsedTimeBeforeStart, elapsedTimeAfterStart)
            viewModel.stop()

            val uiState = viewModel.uiState.value

            val expectedState = initialState.copy(
                startTime = elapsedTimeBeforeStart,
                timeAtPaused = elapsedTimeBeforeStart + elapsedTimeAfterStart,
                stopWatchTime = "00:00.10",
                isPaused = true
            )

            assertEquals(expectedState, uiState)
        } finally {
            viewModel.timerJob?.cancel()
        }
    }

    @Test
    fun afterReset_stateIsUpdatedCorrectly() = runTest { // ストップウォッチをスタートさせ一時停止後にリセットさせたときの状態をテスト
        // テストに失敗する例外を発生させ本来はテストが終わるが、start()内のwhile1が終わっていないため永遠にループする。
        // そのため、try-finallyを用いることで、テスト失敗し例外が発生しても、finallyでコルーチンを終わらせることでテストが終了する。
        try{
            val elapsedTimeBeforeStart = 3000L// システム起動からの時間が3秒経過したことにする
            val elapsedTimeAfterStart = 100L // Whileループ内のdelay1回分の時間を経過させる
            start(elapsedTimeBeforeStart, elapsedTimeAfterStart)
            viewModel.stop()
            viewModel.reset()

            val uiState = viewModel.uiState.value

            val expectedState = initialState

            assertEquals(expectedState, uiState)
        } finally {
            viewModel.timerJob?.cancel()
        }
    }
}