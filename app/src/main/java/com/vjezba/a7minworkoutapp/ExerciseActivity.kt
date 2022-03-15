package com.vjezba.a7minworkoutapp

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import com.vjezba.a7minworkoutapp.databinding.ActivityExerciseBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding:ActivityExerciseBinding? = null
    private var tts: TextToSpeech? = null
    private var tts2: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress: Int = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress: Int = 0
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = 0
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        tts = TextToSpeech(this, this)
        tts2 = TextToSpeech(this, this)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        exerciseList = Constants.defaultExerciseList()
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }
        handler.postDelayed({
            setUpRestView()
        }, 3*1000)

    }

    private fun setUpRestView(){

        speakOut(binding?.nextExercise?.text!!.toString(), exerciseList!![currentExercisePosition].getName())

        try {
            val soundURI = Uri.parse(
                "android.resource://com.vjezba.a7minworkoutapp/"
                        + R.raw.app_src_main_res_raw_press_start)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.apply {
                isLooping = false
                start()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding?.apply {
            groupRest.visibility = View.VISIBLE
            groupExercise.visibility = View.INVISIBLE
            exerciseName.setTextColor(Color.parseColor("red"))
            exerciseName.text = exerciseList!![currentExercisePosition].getName()
            exerciseImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
            nextExercise.visibility = View.VISIBLE
        }
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    private fun setupExerciseView(){

        try {
            val soundURI = Uri.parse(
                "android.resource://com.vjezba.a7minworkoutapp/"
                        + R.raw.app_src_main_res_raw_press_start)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.apply {
                isLooping = false
                start()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding?.apply {
            groupRest.visibility = View.INVISIBLE
            groupExercise.visibility = View.VISIBLE
            nextExercise.visibility = View.INVISIBLE
            exerciseImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        }
        if (exerciseTimer !=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBarRest?.progress = restProgress

        restTimer = object : CountDownTimer(11*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBarRest?.progress = 11 - restProgress
                binding?.restTimeValue?.text = (11 - restProgress).toString()
            }

            override fun onFinish() {
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(31*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 31 - exerciseProgress
                binding?.exerciseTimeValue?.text = (31 - exerciseProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                if (currentExercisePosition <exerciseList?.size!!){
                    setUpRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity, "Good Job!!", Toast.LENGTH_LONG).show()
                }

            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer !=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if (tts2!= null){
            tts2!!.stop()
            tts2!!.shutdown()
        }

        if (player != null){
            player!!.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The Language specified is not Supported!")
            }
        }else{
            Log.e("TTS", "Initialization Failed")
        }
    }

    private fun speakOut(text: String,text2: String){
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        tts2?.speak(text2, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}