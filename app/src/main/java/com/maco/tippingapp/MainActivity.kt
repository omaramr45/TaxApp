    package com.maco.tippingapp

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.core.content.ContextCompat


private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var etBase: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var tvTip: TextView
    private lateinit var tvPercent: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvTaxDescription: TextView
    private lateinit var tvTaxSplit: TextView
    private lateinit var etTaxSplit: EditText
    private lateinit var btnTaxSplit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBase = findViewById(R.id.etBase)
        seekBar = findViewById(R.id.seekBar)
        tvTip = findViewById(R.id.tvTip)
        tvPercent = findViewById(R.id.tvPercent)
        tvTotal = findViewById(R.id.tvTotal)
        tvTaxDescription = findViewById(R.id.tvTaxDescription)
        tvTaxSplit = findViewById(R.id.tvSplitTax)
        btnTaxSplit = findViewById(R.id.btnSplit)
        etTaxSplit = findViewById(R.id.etSplitTax)

        seekBar.progress = INITIAL_TIP_PERCENT
        tvPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTaxDescription(INITIAL_TIP_PERCENT)


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                tvPercent.text = "$progress%"

                computeTaxAndTotal()
                updateTaxDescription(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        etBase.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                computeTaxAndTotal()
            }


        })

        btnTaxSplit.setOnClickListener {

            splitTax()

        }

    }

    private fun splitTax() {

        //Get the value of base, tax percent and number you will split tax on

        val baseAmount = etBase.text.toString().toDouble()
        val taxPercent = seekBar.progress
        var taxAmount = baseAmount * taxPercent / 100
        val etSplit = etTaxSplit.text.toString().toDouble()

        //compute split tax

        val splittedTax = taxAmount / etSplit

        //update UI

        tvTaxSplit.text = "%.2f".format(splittedTax)


    }

    private fun updateTaxDescription(taxPercent: Int) {

        val taxDescription = when (taxPercent) {

            in 0..9 -> "Good"
            in 10..15 -> "Okay"
            in 16..23 -> "high"
            else -> "So High"

        }

        tvTaxDescription.text = taxDescription

        //Update color of tax description

        val color = ArgbEvaluator().evaluate(
            taxPercent.toFloat() / seekBar.max,
            ContextCompat.getColor(this, R.color.color_worst_tax),
            ContextCompat.getColor(this, R.color.color_best_tax)
        )
                as Int
        tvTaxDescription.setTextColor(color)

    }

    private fun computeTaxAndTotal() {


        if (etBase.text.isEmpty()) {

            tvTip.text = ""
            tvTotal.text = ""
            return

        }

        //Get the value of base and tax percent

        val baseAmount = etBase.text.toString().toDouble()
        val taxPercent = seekBar.progress

        // compute tax and total

        val taxAmount = baseAmount * taxPercent / 100
        val totalAmount = baseAmount + taxAmount

        // update UI
        tvTip.text = "%.2f".format(taxAmount)
        tvTotal.text = "%.2f".format(totalAmount)

    }
}