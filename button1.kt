package com.example.mc_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class Activity3 : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var startStationEditText: EditText
    private lateinit var destinationStationEditText: EditText
    private lateinit var busNumberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        latitudeTextView = findViewById(R.id.latitudeTextView)
        longitudeTextView = findViewById(R.id.longitudeTextView)

        startStationEditText = findViewById(R.id.startStationEditText)
        destinationStationEditText = findViewById(R.id.destinationStationEditText)
        busNumberEditText = findViewById(R.id.busNumberEditText)
    }

    fun getLocation(view: android.view.View) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                this
            )
        } catch (ex: SecurityException) {
            Toast.makeText(
                this,
                "Location permission denied. Cannot access location.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onLocationChanged(location: Location) {
        latitudeTextView.text = "Latitude: ${location.latitude}"
        longitudeTextView.text = "Longitude: ${location.longitude}"
        Toast.makeText(this, "Location Updated", Toast.LENGTH_SHORT).show()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    fun onNextButtonClick(view: android.view.View) {
        val startStation = startStationEditText.text.toString()
        val destinationStation = destinationStationEditText.text.toString()
        val busNumber = busNumberEditText.text.toString()

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("START_STATION", startStation)
            putExtra("DESTINATION_STATION", destinationStation)
            putExtra("BUS_NUMBER", busNumber)
        }
        startActivity(intent)
    }
}
