package com.example.stopwatchapp.ui.screens.home

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 本番環境用とテスト用で経過時間を取得する為のインターフェース
interface TimeSource {
    fun getElapsedTime(): Long
}

// 本番環境用に上記TImeSourceを実装し、システムの起動時間からの経過時間を取得
object SystemTimeSource : TimeSource {
    override fun getElapsedTime(): Long {
        return SystemClock.elapsedRealtime()
    }
}

// ストップウォッチのUI状態を表すデータクラス
data class StopWatch(
    val startTime: Long = 0, // 開始時刻or前回の開始時刻と停止中の時間を足したもの
    val timeAtPaused: Long = 0, // 停止時刻
    val stopWatchTime: String = "00:00.00",
    val isRunning: Boolean = false,
    val isPaused: Boolean = false
)

class StopWatchViewModel(private val timeSource: TimeSource) : ViewModel() {
    private val _uiState = MutableStateFlow(StopWatch())
    val uiState: StateFlow<StopWatch> = _uiState.asStateFlow()
    var timerJob: Job? = null

    fun start() {
        if (isRunning()) return // すでに実行中なら以下の処理をせずreturn

        // 停止中 == 前回のスタート時刻と停止中の時間を足したものをスタートタイムとする。
        // 停止中 != 現在の時刻から。
        val startTime: Long = if (isPaused()) {
            val currentTime = timeSource.getElapsedTime()
            val pausedDuration = currentTime - _uiState.value.timeAtPaused
            _uiState.value.startTime + pausedDuration
        } else {
            timeSource.getElapsedTime()
        }

        _uiState.update { currentState ->
            currentState.copy(
                startTime = startTime,
                isRunning = true,
                isPaused = false
            )
        }

        // 計測中（isRunning = true）の間は100ミリ秒ごとにタイムを更新
        timerJob = viewModelScope.launch {
            val timeAtPaused = _uiState.value.timeAtPaused
            val startTime = _uiState.value.startTime

            while (_uiState.value.isRunning) {
                delay(100)
                val currentTime = timeSource.getElapsedTime()
                val stopWatchTime = currentTime - startTime
                val formattedStopWatchTIme = timeFormatter(stopWatchTime)

                _uiState.update {currentState ->
                    currentState.copy( stopWatchTime = formattedStopWatchTIme )
                }
            }
        }
    }

    fun stop() {
        if (isRunning()) {
            timerJob?.cancel() // タイマー処理のwhileループを終わらせる
            val timeAtPaused = timeSource.getElapsedTime()
            _uiState.update {currentState ->
                currentState.copy(
                    timeAtPaused = timeAtPaused,
                    isRunning = false,
                    isPaused = true
                )
            }
        }
    }

    fun reset() {
        if (isPaused()) {
            _uiState.update {currentState ->
                currentState.copy(
                    startTime = 0,
                    timeAtPaused = 0,
                    stopWatchTime = "00:00.00",
                    isRunning = false,
                    isPaused = false
                )
            }
        }
    }

    fun timeFormatter(time: Long): String {
        val hour: Long = time / 1000 / 3600
        val minute: Long = time / 1000 % 3600 / 60
        val second: Long = time / 1000 % 3600 % 60
        val millisecond: Long = time % 1000 / 10

        if (hour >= 1) {
            return "%02d:%02d:%02d.%02d".format(hour, minute, second, millisecond)
        } else {
            return "%02d:%02d.%02d".format(minute, second, millisecond)
        }
    }

    fun isRunning(): Boolean {
        return _uiState.value.isRunning
    }

    fun isPaused(): Boolean {
        return _uiState.value.isPaused
    }


}