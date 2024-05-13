package com.example.mc_project

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Activity5 : AppCompatActivity() {

//    private lateinit var stationRecyclerView: RecyclerView
    private lateinit var stationAdapter: StationAdapter
    private lateinit var stationNames: List<String>
    private lateinit var stopIds: List<String>
    private lateinit var latitude: String
    private lateinit var longitude: String
    private var currentStationIndex: Int = 0
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_5)

        // Retrieve the station names, stop IDs, latitude, and longitude passed from Activity3
        stationNames = intent.getStringArrayListExtra("STATION_NAMES") ?: emptyList()
        stopIds = intent.getStringArrayListExtra("STOP_IDS") ?: emptyList()
        latitude = intent.getStringExtra("LATITUDE") ?: ""
        longitude = intent.getStringExtra("LONGITUDE") ?: ""
        val destinationName = intent.getStringExtra("DESTINATION") ?: ""
        val singleStationName = intent.getStringExtra("SINGLE_STATION_NAME") ?: ""

        // Find views
        recyclerView = findViewById(R.id.stationRecyclerView)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val nextButton = findViewById<Button>(R.id.nextButton)

        // Find the index of the single station name
        var singleStationIndex = stationNames.indexOf(singleStationName)
        Log.d("Startingindex", singleStationIndex.toString())
        if (singleStationIndex == -1) {
            // If the single station name is not found, default to starting from the first station
            singleStationIndex = 0
        }

        // Find the index of the single station name
//        val singleStationIndex = stationNames?.indexOf(singleStationName) ?: 0

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
