package com.zk.timetracker

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.util.Patterns
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

val DATE_TIME_FORM_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.ENGLISH)

@Composable
fun TextFieldDefaults.error(isError: Boolean): TextFieldColors {
    return this.outlinedTextFieldColors(
        focusedBorderColor = if (isError) Color.Red else Color.Black,
        unfocusedBorderColor = if (isError) Color.Red else Color.Black,
        focusedLabelColor = if (isError) Color.Red else Color.Black,
        unfocusedLabelColor = if (isError) Color.Red else Color.Black,
    )
}

class Timer(val passedSeconds: MutableState<Int>, val isPlaying: MutableState<Boolean>) {
    val secondsToEndOfTheDay = 999999999L
    private val countDownTimer: CountDownTimer =
        object : CountDownTimer(secondsToEndOfTheDay * 1000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isPlaying.value) {
                    cancel()
                    onFinish()
                } else {
                    passedSeconds.value += 1
                }
            }

            override fun onFinish() {
                isPlaying.value = false
                passedSeconds.value = 0
            }
        }

    fun play() {
        isPlaying.value = true
        countDownTimer.start()
    }

    fun stop(callback: () -> Unit) {
        callback()
        isPlaying.value = false
        passedSeconds.value = 0
        countDownTimer.cancel()
    }
}