package com.vjezba.a7minworkoutapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vjezba.a7minworkoutapp.HistoryAdapter
import com.vjezba.a7minworkoutapp.common.HistoryDao
import com.vjezba.a7minworkoutapp.common.WorkoutApp
import com.vjezba.a7minworkoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHistoryActivity)
        if (supportActionBar != null) {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = "History"
            }
        }
        binding.toolbarHistoryActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        val historyDao = (application as WorkoutApp).db.historyDao()
        getAllCompletedDates(historyDao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { allComplitedDatesList ->
                if (allComplitedDatesList.isNotEmpty()) {
                    binding.apply {
                        tvHistory.visibility = View.VISIBLE
                        rvHistory.visibility = View.VISIBLE
                        tvNoDataAvailable.visibility = View.INVISIBLE
                        rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    }
                    val dates = ArrayList<String>()
                    for (date in allComplitedDatesList) {
                        dates.add(date.date)
                    }
                    val historyAdapter = HistoryAdapter(dates)
                    binding.rvHistory.adapter = historyAdapter
                } else {
                    binding.apply {
                        tvHistory.visibility = View.GONE
                        rvHistory.visibility = View.GONE
                        tvNoDataAvailable.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}