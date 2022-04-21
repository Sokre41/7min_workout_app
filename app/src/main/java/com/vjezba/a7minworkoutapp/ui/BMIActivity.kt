package com.vjezba.a7minworkoutapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.vjezba.a7minworkoutapp.R
import com.vjezba.a7minworkoutapp.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode


class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val STATE_UNITS_VIEW = "STATE_UNIT_VIEW"
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    private val binding by lazy { ActivityBmiactivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarBmi)
        if (supportActionBar != null) {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = "Calculate BMI"
            }
        }
        binding.toolbarBmi.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding.unitTypeSelector.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.unit_selector_metric){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUsUnitsView()
            }
        }

        binding.calculateYourBmi.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW){
                calculateMetricBmi()
            }else{
                calculateUsBmi()
            }
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding.apply {
            metricUnitGroup.visibility = View.VISIBLE
            usUnitGroup.visibility = View.INVISIBLE
            usInputWeight.text!!.clear()
            usInputFeet.text!!.clear()
            usInputInch.text!!.clear()
            bmiResultTextGroup.visibility = View.INVISIBLE
        }
    }

    private fun makeVisibleUsUnitsView(){
        currentVisibleView = STATE_UNITS_VIEW
        binding.apply {
            metricUnitGroup.visibility = View.INVISIBLE
            usUnitGroup.visibility = View.VISIBLE
            metricInputWeight.text!!.clear()
            metricInputHeight.text!!.clear()
            bmiResultTextGroup.visibility = View.INVISIBLE
        }
    }

    private fun validateMetricUnit(): Boolean{
        var isValid = true
        if(binding.metricInputWeight.text.toString().isEmpty()){
            isValid = false
        }else if(binding.metricInputHeight.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateUsUnit(): Boolean{
        var isValid = true
        if(binding.usInputWeight.text.toString().isEmpty()){
            isValid = false
        }else if(binding.usInputFeet.text.toString().isEmpty()){
            isValid = false
        }else if(binding.usInputInch.text.toString().isEmpty()){
        isValid = false
    }
        return isValid
    }

    private fun calculateMetricBmi(){
        if (validateMetricUnit()){
            val heightMetricValue : Float = binding.metricInputHeight.text.toString().toFloat()
            val weightMetricValue : Float = binding.metricInputWeight.text.toString().toFloat()
            val bmiMetric = (weightMetricValue / heightMetricValue / heightMetricValue) * 10000
            displayBMIResults(bmiMetric)
        }else{
            Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_LONG).show()
        }
    }

    private fun calculateUsBmi(){
        if (validateUsUnit()){
            val weightUsValue : Float = binding.usInputWeight.text.toString().toFloat()
            val feetUsValue : String = binding.usInputFeet.text.toString()
            val inchUsValue : String = binding.usInputInch.text.toString()
            val heightUsValue = inchUsValue.toFloat() + feetUsValue.toFloat() * 12
            val bmiUs = (weightUsValue * 703) / (heightUsValue * heightUsValue)
            displayBMIResults(bmiUs)
        }else{
            Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_LONG).show()
        }
    }

    private fun displayBMIResults(bmi : Float){

        val bmiLabel : String
        val bmiDescription : String

        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight!!"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more and healthy!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(17f) <= 0 ){
            bmiLabel = "Moderate underweight!!"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more and healthy!"
        }else if (bmi.compareTo(17f) > 0 && bmi.compareTo(18.5f) <= 0 ){
            bmiLabel = "Mild underweight!!"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more and healthy!"
        }else if (bmi.compareTo(18.5) > 0 && bmi.compareTo(25f) <= 0 ) {
            bmiLabel = "Normal weight "
            bmiDescription = "Congratulation! Your are in good shape!"
        }else if (bmi.compareTo(25) > 0 && bmi.compareTo(30f) <= 0 ){
            bmiLabel = "Overweight!!"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout more and eat healthy!"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0 ){
            bmiLabel = "Moderate overweight!!"
            bmiDescription = "OMG! You really need to take better care of yourself! Workout more and eat healthy!"
        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0 ) {
            bmiLabel = "Highly overweight!!"
            bmiDescription = "OMG! You really need to take better care of yourself! Workout more and eat healthy!"
        }else{
            bmiLabel = "Dangerously overweight!!"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding.apply {
            bmiResultTextGroup.visibility = View.VISIBLE
            yourBmiResult.text = bmiValue
            yourBmiLabel.text = bmiLabel
            yourBmiDescription.text = bmiDescription
        }

    }
}