package com.example.swe_426_project_1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var fromUnitLabel: TextView
    private lateinit var fromUnitValue: TextView
    private lateinit var toUnitLabel: TextView
    private lateinit var toUnitValue: TextView

    data class Conversion(
        val label: String,
        val fromUnit: String,
        val toUnit: String,
        val ratio: Double
    )

    private val conversions = listOf(
        Conversion("Miles to Kilometers", "Miles", "Kilometers", 1.6093),
        Conversion("Kilometers to Miles", "Kilometers", "Miles", 0.6214),
        Conversion("Inches to Centimeters", "Inches", "Centimeters", 2.54),
        Conversion("Centimeters to Inches", "Centimeters", "Inches", 0.3937),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinner = findViewById(R.id.spinner)
        fromUnitLabel = findViewById(R.id.fromUnitLabel)
        fromUnitValue = findViewById(R.id.fromUnitValue)
        toUnitLabel = findViewById(R.id.toUnitLabel)
        toUnitValue = findViewById(R.id.toUnitValue)

        setupSpinner()
        setupTextWatcher()
    }

    private fun setupTextWatcher() {
        fromUnitValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                performConversion()
            }
        })
    }

    private fun setupSpinner() {
        val labels = conversions.map { it.label }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, labels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateUnitLabels(position)
                performConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>) { /* no-op */ }
        }
    }

    private fun updateUnitLabels(position: Int) {
        val selected = conversions[position]

        fromUnitLabel.text = selected.fromUnit
        toUnitLabel.text = selected.toUnit
    }

    private fun performConversion() {
        val inputText = fromUnitValue.text.toString()

        if (inputText.isEmpty()) {
            toUnitValue.text = ""
            return
        }

        val inputValue = inputText.toDoubleOrNull()

        if (inputValue == null) {
            toUnitValue.text = "Invalid Input"
            return
        }

        val selected = conversions[spinner.selectedItemPosition]

        toUnitValue.text = "%.3f".format(inputValue * selected.ratio).trimEnd('0').trimEnd('.')

    }
}