package com.example.mc_project

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.BufferedReader
import java.io.InputStreamReader


data class Stops(val code: String, val id: String, val lat: Double, val lon: Double, val name: String, val zoneId: String)

data class Routes(val routeId: String, val stopIds: String)

//data class RouteInfo(val routeId: String, val stopIds: List<String>)

class Activity3 : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var startStationSpinner: Spinner
    private lateinit var destinationStationSpinner: Spinner
//    private lateinit var busNumberEditText: EditText
    private lateinit var stops: List<Stops>
    private lateinit var routes: List<Routes>
    private lateinit var routesContainer: LinearLayout


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        latitudeTextView = findViewById(R.id.latitudeTextView)
        longitudeTextView = findViewById(R.id.longitudeTextView)

        startStationSpinner = findViewById(R.id.startStationSpinner)
        destinationStationSpinner = findViewById(R.id.destinationStationSpinner)
        routesContainer = findViewById(R.id.routesContainer) // Add this line to initialize routesContainer

//        busNumberEditText = findViewById(R.id.busNumberEditText)

        val stationNamesArray = resources.getStringArray(R.array.station_names)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stationNamesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        startStationSpinner.adapter = adapter
        destinationStationSpinner.adapter = adapter

        stops = loadStopsFromCSV()
        routes = loadRoutesFromCSV()
        if (routes.isNotEmpty()) {
            val firstRoute = routes[0]
            Log.d("FirstRouteList", firstRoute.toString())
        }
        val gmapButton = findViewById<Button>(R.id.gmapButton)
        gmapButton.setOnClickListener {
            val startStation = startStationSpinner.selectedItem.toString()
            val destinationStation = destinationStationSpinner.selectedItem.toString()
            val startStop = searchStation(startStation)
            val endStop = searchStation(destinationStation)

            val startStopId = startStop?.id ?: "Not found"
            val endStopId = endStop?.id ?: "Not found"
            val routeInfo = findRoute(startStopId, endStopId)
            val routeId = routeInfo?.routeId
            val stopIds = routeInfo?.stopIds?.joinToString(", ")
//            val latitudeLongitudeList = stopIds?.mapNotNull { getLatitudeLongitudeForStopId(it.toString()) }
            val stationNames = findStationNames(routeInfo!!.stopIds)


            val latitudeLongitudeList = stopIds?.mapNotNull { getLatitudeLongitudeForStopId(it.toString()) }
            val latLongStrings = latitudeLongitudeList?.map { "${it.first},${it.second}" }

            val intent = Intent(this, gmap::class.java).apply{
                putStringArrayListExtra("STATION_NAMES_MAP", ArrayList(stationNames))
                putStringArrayListExtra("STOP_IDS_MAP", ArrayList(routeInfo.stopIds))
                putStringArrayListExtra("STOP_LAT_LONG", ArrayList(latLongStrings))
                putExtra("SINGLE_STATION_NAME", startStation) // Pass the single station name
                putExtra("DESTINATION", destinationStation)
            }
            startActivity(intent)


        }
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
        try {
            val startStation = startStationSpinner.selectedItem.toString()
            val destinationStation = destinationStationSpinner.selectedItem.toString()

            val startStop = searchStation(startStation)
            val endStop = searchStation(destinationStation)

            // Get stop IDs for start and end stations
            val startStopId = startStop?.id ?: "Not found"
            val endStopId = endStop?.id ?: "Not found"

            val matchingRoutes = findRoutesWithStopNames(startStopId, endStopId)
            Log.e("ALL route", matchingRoutes.toString())
            // Clear previous route views
            routesContainer.removeAllViews()

            // Add TextViews for each matching route
            for (route in matchingRoutes) {
                val routeTextView = createRouteTextView(route)
                routeTextView.setOnClickListener {
                    // Handle click event
                    val routeId = route.first
                    val startStopName = route.second.first
                    val endStopName = route.second.second
                    val ListofStation = route.third
                    val StationList = findStationNames(ListofStation)
                    Log.e("RD1",routeId)
                    Log.e("RD2",startStopName)
                    Log.e("RD3",endStopName)
                    Log.e("RD4", StationList.toString())
                    Log.e("RD5", ListofStation.toString())

                    // Start Activity6
                    val intent = Intent(this, Activity6::class.java).apply {
                        // Pass necessary data to Activity6
                        putStringArrayListExtra("STATION_NAMES", java.util.ArrayList(StationList))
                        putStringArrayListExtra("STOP_IDS", java.util.ArrayList(ListofStation))
                        putExtra("LATITUDE", latitudeTextView.text.toString())
                        putExtra("LONGITUDE", longitudeTextView.text.toString())
                        putExtra("SINGLE_STATION_NAME", startStation) // Pass the single station name
                        putExtra("DESTINATION", destinationStation) // Pass the single station name
                    }
                    startActivity(intent)
                }
                routesContainer.addView(routeTextView)
            }

        }
        catch (e: Throwable) {
            Log.e("NextButtonClickError", "An error occurred in onNextButtonClick: ${e.message}", e)
            // Handle the error, display a toast, or take any appropriate action
        }
    }
    private fun findStationNames(stopIds: List<String>): List<String> {
        val stationNames = mutableListOf<String>()
        for (stopId in stopIds) {
            val stop = stops.find { it.id == stopId }
            stop?.let {
                stationNames.add(it.name)
            }
        }
        return stationNames
    }
    private fun getLatitudeLongitudeForStopId(stopId: String): Pair<Double, Double>? {
        val stop = stops.find { it.id == stopId }
        return stop?.let { it.lat to it.lon }
    }
    private fun createRouteTextView(route: Triple<String, Pair<String, String>, List<String>>): TextView {
        val textView = TextView(this)
        textView.text = "Route ID: ${route.first}\n" +
                "First Stop Name: ${route.second.first}\n" +
                "Last Stop Name: ${route.second.second}\n\n"
        textView.setTypeface(null, Typeface.BOLD)
        return textView
    }

    private fun loadStopsFromCSV(): List<Stops> {
        val stopsList = mutableListOf<Stops>()
        val inputStream = resources.openRawResource(R.raw.stops)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.use { reader ->
            var line: String?
            reader.readLine() // Skip the header line
            while (reader.readLine().also { line = it } != null) {
                val parts = line!!.split(",")
                val stop = Stops(parts[0], parts[1], parts[2].toDouble(), parts[3].toDouble(), parts[4], parts[5])
                stopsList.add(stop)
            }
        }
        return stopsList
    }

    private fun loadRoutesFromCSV(): List<Routes> {
        val routesList = mutableListOf<Routes>()
        val inputStream = resources.openRawResource(R.raw.output)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.use { reader ->
            var line: String?
            reader.readLine() // Skip the header line
            while (reader.readLine().also { line = it } != null) {
                val parts = line!!.split(",")
                val routeId = parts[0]
                val stopIds = parts.subList(1, parts.size).joinToString(",") // Join stopIds into a single string
                val route = Routes(routeId, stopIds)
                routesList.add(route)
            }
        }
        return routesList
    }

    private fun searchStation(name: String): Stops? {
        return stops.find { it.name == name }
    }
    private data class RouteInfo(val routeId: String, val stopIds: List<String>)

    private fun findRoute(startStopId: String, endStopId: String): RouteInfo? {
        // Loop through each route
        for (route in routes) {
            // Extract route ID and stop IDs from the route object
            val routeId = route.routeId
            val stopIds = route.stopIds

            // Remove brackets from stopIds and split into individual IDs
            val cleanedStopIds = stopIds.replace("[", "").replace("]", "").replace(" ", "")
            val stopIdList = cleanedStopIds.split(",") // Split by comma

            // Convert startStopId and endStopId to integers
            val startId = startStopId.toInt()
            val endId = endStopId.toInt()

            // Check if startStopId and endStopId are in stopIds
            val startIndex = stopIdList.indexOf(startId.toString())
            val endIndex = stopIdList.indexOf(endId.toString())

            if (startIndex != -1 && endIndex != -1) {
                // Start and end stop IDs found in route
                // Check if they are consecutive or in reverse order
                if (startIndex < endIndex) {
                    // Found a valid route
                    return RouteInfo(routeId, stopIdList)
                }
            }
        }

        // If no route found
        return null
    }
private fun findRoutesWithStopNames(startStopId: String, endStopId: String): List<Triple<String, Pair<String, String>, List<String>>> {
    val matchingRoutes = mutableListOf<Triple<String, Pair<String, String>, List<String>>>()

    // Loop through each route
    for (route in routes) {
        // Extract route ID and stop IDs from the route object
        val routeId = route.routeId
        val stopIds = route.stopIds

        // Remove brackets from stopIds and split into individual IDs
        val cleanedStopIds = stopIds.replace("[", "").replace("]", "").replace(" ", "")
        val stopIdList = cleanedStopIds.split(",") // Split by comma

        // Convert startStopId and endStopId to integers
        val startId = startStopId.toInt()
        val endId = endStopId.toInt()

        // Check if startStopId and endStopId are in stopIds
        val startIndex = stopIdList.indexOf(startId.toString())
        val endIndex = stopIdList.indexOf(endId.toString())

        if (startIndex != -1 && endIndex != -1) {
            // Start and end stop IDs found in route
            // Check if they are consecutive or in reverse order
            if (startIndex < endIndex) {
                // Found a valid route
                val firstStopId = stopIdList.first().trim('"')
                val lastStopId = stopIdList.last().trim('"')

                val firstName = find1stStationName(firstStopId)
                val lastName = find1stStationName(lastStopId)

                val routeInfo = Triple(routeId, Pair(firstName, lastName), stopIdList)
                matchingRoutes.add(routeInfo as Triple<String, Pair<String, String>, List<String>>)
            }
        }
    }

    // If no routes found, matchingRoutes will be empty
    return matchingRoutes
}
private fun find1stStationName(stopId: String): String? {
    val stop = stops.find { it.id == stopId }
    return stop?.name
}
}

