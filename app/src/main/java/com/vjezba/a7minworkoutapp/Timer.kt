package com.vjezba.a7minworkoutapp

import android.os.CountDownTimer

class Timer(
    millisInFuture: Long,
    countDownInterval: Long,
    val timerId: String,
    val listener: TimerCallback
) : CountDownTimer(millisInFuture, countDownInterval) {

    interface TimerCallback {
        fun timerProgressAdvanced(timerId: String, progressSec: Int)

        fun timerFinished(timerId: String)
    }

    private var progressSec: Int = 0

    override fun onTick(p0: Long) {
        progressSec++
        listener.timerProgressAdvanced(timerId, progressSec)
    }

    override fun onFinish() {
        listener.timerFinished(timerId)
    }

    fun resetTimer() {
        progressSec = 0
        cancel()
    }
}