package com.txhien.numberanimation

import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.reflect.Method
import kotlin.math.abs
import kotlin.random.Random

/**
 * Created by Hita on 24,March,2022
 **/

class MainActivity : AppCompatActivity() {
    companion object {
        private const val MIN_VALUE = 0
        private const val MAX_VALUE = 50
        private const val DEFAULT_VALUE = 30
        private const val TIMER_DELAY = 70L
    }

    private var animateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()
        numberPicker.setOnClickListener {
            handleClickNumberPicker()
        }
    }

    private fun initNumberPicker() {
        numberPicker.minValue = MIN_VALUE
        numberPicker.maxValue = MAX_VALUE

        numberPicker.value = DEFAULT_VALUE
    }

    private fun handleClickNumberPicker() {
        val desValue = Random.nextInt(MIN_VALUE, MAX_VALUE)
        val currentValue = numberPicker.value

        Toast.makeText(this, "Current number: $currentValue, Next number: $desValue", Toast.LENGTH_SHORT).show()
        val isIncrease = (desValue - numberPicker.value) > 0

        animationNumberPicker(desValue, currentValue, isIncrease)
    }

    private fun animationNumberPicker(desValue: Int, currentValue: Int, isIncrease: Boolean) {
        animateJob = CoroutineScope(Dispatchers.Main).launch {
            var loop = 0
            while (loop < abs(desValue - currentValue)) {
                changeValueNumberPicker(numberPicker, isIncrease)
                loop++
                delay(TIMER_DELAY)
            }
            animateJob?.cancel()
        }
        animateJob?.start()
    }

    private fun changeValueNumberPicker(numberPicker: NumberPicker, isIncreasing: Boolean) {
        val method: Method
        try {
            method = numberPicker.javaClass.getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method.isAccessible = true
            method.invoke(numberPicker, isIncreasing)
        } catch (e: Exception) {
            Log.e("changeValueNumberPicker", e.toString())
        }
    }
}