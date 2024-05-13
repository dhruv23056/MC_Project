package com.example.mc_project

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Activity6 : AppCompatActivity() {

    private lateinit var currentTimeTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var seekBar: SeekBar
    private lateinit var nextButton: Button
    private var currentStationIndex = 0 // Track current station index

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_6)

        currentTimeTextView = findViewById(R.id.currentTimeTextView)
        recyclerView = findViewById(R.id.recyclerView)
        seekBar = findViewById(R.id.seekBar)
        nextButton = findViewById(R.id.nextButton)

        // Retrieve the station names, stop IDs, and current time passed from Activity4
        val stationNames = intent.getStringArrayListExtra("STATION_NAMES")
        val stopIds = intent.getStringArrayListExtra("STOP_IDS")
        val currentTime = intent.getStringExtra("CURRENT_TIME")
        val singleStationName = intent.getStringExtra("SINGLE_STATION_NAME")
        val destinationName = intent.getStringExtra("DESTINATION")

        currentTimeTextView.text = "Current Time: $currentTime"

        // Find the index of the single station name
        val singleStationIndex = stationNames?.indexOf(singleStationName) ?: 0

        // Initialize and set up the RecyclerView with StationAdapter
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = StationAdapter(stationNames ?: emptyList(), stopIds ?: emptyList(), destinationName,singleStationName)
        recyclerView.adapter = adapter

        // Set up seek bar
        seekBar.max = stationNames?.size ?: 0
        seekBar.progress = currentStationIndex

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update current station index when seek bar changes
                currentStationIndex = progress
                adapter.setCurrentStationIndex(currentStationIndex)
                recyclerView.scrollToPosition(currentStationIndex)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Set up next button click listener
        nextButton.setOnClickListener {
            if (currentStationIndex < (stationNames?.size ?: 0) - 1) {
                currentStationIndex++
                seekBar.progress = currentStationIndex
            }
        }

        // Scroll to the single station index
        recyclerView.scrollToPosition(singleStationIndex)
        seekBar.progress = singleStationIndex
    }
}
