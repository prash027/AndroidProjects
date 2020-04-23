package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG="MainActivity"
private const val INITIAL_TIP_PERCENT = 15


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercent.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etBaseLabel.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription : String
        when (tipPercent) {
            in 0..9-> tipDescription="☹️"
            in 10..14 -> tipDescription="\uD83D\uDE10"
            in 15..19 -> tipDescription="☺️"
            in 20..24 -> tipDescription="\uD83D\uDE00"
            else -> tipDescription="❤️"
        }
        tvTipDescription.text=tipDescription
        val color = ArgbEvaluator().evaluate(tipPercent.toFloat()/ seekBarTip.max,
            ContextCompat.getColor(this,R.color.colorWorstTip),
            ContextCompat.getColor(this,R.color.colorBestTip)
        )as Int
        tvTipDescription.setTextColor(color)

    }

    private fun computeTipAndTotal() {
        //get the value of the base and tip percent
        if (etBaseLabel.text.isEmpty()){
            tvTipAmtLabel.text=""
            tvTotAmtLabel.text=""
            return
        }
        val baseAmount= etBaseLabel.text.toString().toDouble()
        val tipPercent= seekBarTip.progress
        val tipAmount= baseAmount*tipPercent/100
        val totalAmount = baseAmount + tipAmount
        tvTipAmtLabel.text="%.2f".format(tipAmount)
        tvTotAmtLabel.text="%.2f".format(totalAmount)

    }
}
