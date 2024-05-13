package com.example.mc_project

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class gmap: AppCompatActivity(), OnMapReadyCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmap)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val coordinatesList = listOf(
            Pair(28.6129, 77.2295), // India Gate, New Delhi
            Pair(28.5535, 77.2588), // Lotus Temple, New Delhi
            Pair(28.5245, 77.1855), // Qutub Minar, New Delhi
            Pair(28.6127, 77.2773)  // Akshardham Temple, New Delhi
        )
        val latLongStrings = intent.getStringArrayListExtra("STOP_LAT_LONG")
        val latitudeLongitudeList = latLongStrings?.map {
            val (latitudeStr, longitudeStr) = it.split(",")
            Pair(latitudeStr.toDouble(), longitudeStr.toDouble())
        }
        Log.e("LATLONG", latitudeLongitudeList.toString())
        googleMap.setOnMarkerClickListener { marker ->
            // Handle marker click event here
            false
        }

        latitudeLongitudeList?.forEach { (latitude, longitude) ->
            val markerOptions = MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title("Marker at ($latitude, $longitude)")
            googleMap.addMarker(markerOptions)
        }

        // Set camera position to the first marker and zoom in
        val firstMarkerPosition =
            latitudeLongitudeList?.first()
                ?.let { LatLng(it.first, latitudeLongitudeList.first().second) }
        firstMarkerPosition?.let { CameraUpdateFactory.newLatLngZoom(it, 12f) }
            ?.let { googleMap.moveCamera(it) }
    }
}
