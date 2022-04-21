package com.vjezba.a7minworkoutapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vjezba.a7minworkoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.outerCircleStart.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }
        binding.circleBmi.setOnClickListener {
            val intentBMI = Intent(this, BMIActivity::class.java)
            startActivity(intentBMI)
        }
        binding.circleHistory.setOnClickListener {
            val intentHistory = Intent(this, HistoryActivity::class.java)
            startActivity(intentHistory)
        }

    }
}