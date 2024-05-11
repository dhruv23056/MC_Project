package com.example.mc_project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class Activity4 : AppCompatActivity() {

    private lateinit var startStationEditText: EditText
    private lateinit var endStationEditText: EditText
    private lateinit var currentTimeTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)

        startStationEditText = findViewById(R.id.startStationEditText)
        endStationEditText = findViewById(R.id.endStationEditText)
        currentTimeTextView = findViewById(R.id.currentTimeTextView)
    }

    fun getCurrentTime(view: View) {
        val startStation = startStationEditText.text.toString()
        val endStation = endStationEditText.text.toString()

        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedTime = sdf.format(currentTime)

        val currentTimeString = "Current Time: $formattedTime\nStart Station: $startStation\nEnd Station: $endStation"
        currentTimeTextView.text = currentTimeString
    }

    fun onNextButtonClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}