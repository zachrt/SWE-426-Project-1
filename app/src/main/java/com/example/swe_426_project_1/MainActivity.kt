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
    // Sets up variables for the elements on screen
    private lateinit var spinner: Spinner
    private lateinit var fromUnitLabel: TextView
    private lateinit var fromUnitValue: TextView
    private lateinit var toUnitLabel: TextView
    private lateinit var toUnitValue: TextView


    // Creates Conversion class
    data class Conversion(
        val label: String,
        val fromUnit: String,
        val toUnit: String,
        val ratio: Double
    )

    // Creates conversions for the spinner
    private val conversions = listOf(
        Conversion("Miles to Kilometers", "Miles", "Kilometers", 1.6093),
        Conversion("Kilometers to Miles", "Kilometers", "Miles", 0.6214),
        Conversion("Inches to Centimeters", "Inches", "Centimeters", 2.54),
        Conversion("Centimeters to Inches", "Centimeters", "Inches", 0.3937),
        Conversion("Pounds to Kilograms", "Pounds", "Kilograms", 0.453592),
        Conversion("Kilograms to Pounds", "Kilograms", "Pounds", 2.20462),
        Conversion("Ounces to Grams", "Ounces", "Grams", 28.3495),
        Conversion("Grams to Ounces", "Grams", "Ounces", 0.035274)
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

    // Checks input field for text and performs conversion on new text
    private fun setupTextWatcher() {
        fromUnitValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                performConversion()
            }
        })
    }


    // Builds spinner and sets up listener
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

    // Updates the to and from labels based off spinner
    private fun updateUnitLabels(position: Int) {
        val selected = conversions[position]

        fromUnitLabel.text = selected.fromUnit
        toUnitLabel.text = selected.toUnit
    }

    private fun performConversion() {
        val inputText = fromUnitValue.text.toString()

        // Checks to see input is null and sets the output to empty string if it is
        if (inputText.isEmpty()) {
            toUnitValue.text = ""
            return
        }

        // Sets inputValue to a Double or Null type
        val inputValue = inputText.toDoubleOrNull()

        // Check to see if input is a Double or if over 1 billion
        if (inputValue == null) {
            toUnitValue.text = "Invalid Input"
            return
        } else if ( inputValue > 1000000000) {
            toUnitValue.text = "Input Too High"
            return
        }

        // Gets ration from the selected item n the spinner
        val selectedRatio = conversions[spinner.selectedItemPosition].ratio

        // Does the maths
        toUnitValue.text = "%.3f".format(inputValue * selectedRatio).trimEnd('0').trimEnd('.')

    }
}