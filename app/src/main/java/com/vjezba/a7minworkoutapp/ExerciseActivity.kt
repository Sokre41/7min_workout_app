package com.vjezba.a7minworkoutapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.vjezba.a7minworkoutapp.databinding.ActivityExerciseBinding



class ExerciseActivity : AppCompatActivity() {

    private var binding:ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress: Int = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress: Int = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //tv_timer.text = "${(restTimer/1000).toString()}"

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpRestView()


    }

    private fun setUpRestView(){
        binding?.tvExerciseName?.setTextColor(Color.parseColor("red"))

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvGetReadyFor?.visibility = View.INVISIBLE
        binding?.ivExerciseImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        binding?.flExerciseView?.visibility = View.VISIBLE
        if (exerciseTimer !=null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }


        setExerciseProgressBar()
    }


    private fun setRestProgressBar(){
        binding?.progressBarRest?.progress = restProgress

        restTimer = object : CountDownTimer(10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBarRest?.progress = 10 - restProgress
                binding?.tvRestTime?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvExerciseTime?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,"Bla", Toast.LENGTH_LONG).show()
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

        binding = null
    }
}