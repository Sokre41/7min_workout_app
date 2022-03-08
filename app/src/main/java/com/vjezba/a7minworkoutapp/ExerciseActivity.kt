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
    private var currentExercisePosition = 0

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
        binding?.exerciseName?.setTextColor(Color.parseColor("red"))

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
            binding?.groupExercise?.visibility = View.INVISIBLE
        }
        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.apply {
            groupRest.visibility = View.INVISIBLE
            groupExercise.visibility = View.VISIBLE
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

        restTimer = object : CountDownTimer(10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBarRest?.progress = 10 - restProgress
                binding?.restTimeValue?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
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
                binding?.exerciseTimeValue?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
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