package com.vjezba.a7minworkoutapp.ui

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.vjezba.a7minworkoutapp.ExerciseModel
import com.vjezba.a7minworkoutapp.ExerciseStatusAdapter
import com.vjezba.a7minworkoutapp.R
import com.vjezba.a7minworkoutapp.Timer
import com.vjezba.a7minworkoutapp.common.Constants
import com.vjezba.a7minworkoutapp.databinding.ActivityExerciseBinding
import com.vjezba.a7minworkoutapp.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity(),
    TextToSpeech.OnInitListener,
    Timer.TimerCallback {

    companion object {
        private const val REST_TIMER_ID = "REST_TIMER_ID"
        private const val EXERCISE_TIMER_ID = "EXERCISE_TIMER_ID"
        private const val REST_DURATION_TIME_SEC = 11
        private const val EXERCISE_DURATION_TIME_SEC = 31
    }

    private val binding by lazy { ActivityExerciseBinding.inflate(layoutInflater) }

    private val restTimer = Timer(
        millisInFuture = REST_DURATION_TIME_SEC * 1000L,
        countDownInterval = 1000,
        timerId = REST_TIMER_ID,
        listener = this
    )
    private val exerciseTimer = Timer(
        millisInFuture = EXERCISE_DURATION_TIME_SEC * 1000L,
        countDownInterval = 1000,
        timerId = EXERCISE_TIMER_ID,
        listener = this
    )
    private var handler = Handler(Looper.getMainLooper())
    private var exerciseAdapter: ExerciseStatusAdapter? = null
    private var tts: TextToSpeech? = null
    private var tts2: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tts = TextToSpeech(this, this)
        tts2 = TextToSpeech(this, this)

        setSupportActionBar(binding.toolbarExercise)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbarExercise.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        exerciseList = Constants.defaultExerciseList()

        handler.postDelayed({
            setUpRestView()
        }, 1 * 1000)

        setupExerciseStatusRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        restTimer.cancel()
        exerciseTimer.cancel()
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (tts2 != null) {
            tts2!!.stop()
            tts2!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e("TTS", "The Language specified is not Supported!")
            }
        } else {
            Log.e("TTS", "Initialization Failed")
        }
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    override fun timerProgressAdvanced(timerId: String, progressSec: Int) {
        when (timerId) {
            REST_TIMER_ID -> binding.apply {
                progressBarRest.progress = REST_DURATION_TIME_SEC - progressSec
                restTimeValue.text = (REST_DURATION_TIME_SEC - progressSec).toString()
            }

            EXERCISE_TIMER_ID -> binding.apply {
                progressBarExercise.progress = EXERCISE_DURATION_TIME_SEC - progressSec
                exerciseTimeValue.text = (EXERCISE_DURATION_TIME_SEC - progressSec).toString()
            }
        }
    }

    override fun timerFinished(timerId: String) {
        when (timerId) {
            REST_TIMER_ID -> {
                exerciseList!![currentExercisePosition].isSelected = true
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
            EXERCISE_TIMER_ID -> {
                exerciseList!![currentExercisePosition].isSelected = false
                exerciseList!![currentExercisePosition].isCompleted = true
                exerciseAdapter!!.notifyDataSetChanged()
                currentExercisePosition++
                if (currentExercisePosition < exerciseList?.size!!) {
                    setUpRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setUpRestView() {
        speakOut(
            binding.nextExerciseLabel.text!!.toString(),
            exerciseList!![currentExercisePosition].name
        )

        try {
            val soundURI = Uri.parse(
                "android.resource://com.vjezba.a7minworkoutapp/"
                        + R.raw.app_src_main_res_raw_press_start
            )
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.apply {
                isLooping = false
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.apply {
            groupRest.visibility = View.VISIBLE
            groupExercise.visibility = View.INVISIBLE
            exerciseName.text = exerciseList!![currentExercisePosition].name
            exerciseImage.setImageResource(exerciseList!![currentExercisePosition].image)
            nextExerciseLabel.visibility = View.VISIBLE
        }
        restTimer.resetTimer()
        setRestProgressBar()
    }

    private fun setupExerciseView() {
        try {
            val soundURI = Uri.parse(
                "android.resource://com.vjezba.a7minworkoutapp/"
                        + R.raw.app_src_main_res_raw_press_start
            )
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.apply {
                isLooping = false
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.apply {
            groupRest.visibility = View.INVISIBLE
            groupExercise.visibility = View.VISIBLE
            nextExerciseLabel.visibility = View.INVISIBLE
            exerciseImage.setImageResource(exerciseList!![currentExercisePosition].image)
        }
        exerciseTimer.resetTimer()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        binding.progressBarRest.progress = REST_DURATION_TIME_SEC
        restTimer.start()
    }

    private fun setExerciseProgressBar() {
        binding.progressBarExercise.progress = EXERCISE_DURATION_TIME_SEC
        exerciseTimer.start()
    }

    private fun setupExerciseStatusRecyclerView() {
        binding.exerciseStatus.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding.exerciseStatus.adapter = exerciseAdapter
    }

    private fun speakOut(text: String, text2: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        tts2?.speak(text2, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.apply {
            yesAnswer.setOnClickListener {
                this@ExerciseActivity.finish()
                customDialog.dismiss()
            }
            noAnswer.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.show()
        }
    }
}