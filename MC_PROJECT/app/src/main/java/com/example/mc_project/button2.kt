package com.example.mc_project

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

data class Stop(val code: String, val id: String, val lat: Double, val lon: Double, val name: String, val zoneId: String)

data class Route(val routeId: String, val stopIds: String)

data class RouteInfo(val routeId: String, val stopIds: List<String>)

class Activity4 : AppCompatActivity() {

    private lateinit var startStationSpinner: Spinner
    private lateinit var endStationSpinner: Spinner
    private lateinit var currentTimeTextView: TextView
    private lateinit var stops: List<Stop>
    private lateinit var routes: List<Route>
// Inside the Activity4 class

    private lateinit var routesContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)

        // Initialize views
        startStationSpinner = findViewById(R.id.startStationSpinner)
        endStationSpinner = findViewById(R.id.endStationSpinner)
        currentTimeTextView = findViewById(R.id.currentTimeTextView)
        routesContainer = findViewById(R.id.routesContainer) // Add this line to initialize routesContainer

        // Load stops and routes from CSV files
        stops = loadStopsFromCSV()
        routes = loadRoutesFromCSV()
        if (routes.isNotEmpty()) {
            val firstRoute = routes[0]
            Log.d("FirstRouteList", firstRoute.toString())
        }
    }

    fun getCurrentTime(view: View) {
        try {
            val startStationName = startStationSpinner.selectedItem.toString()
            val endStationName = endStationSpinner.selectedItem.toString()

            // Get the current time
            val currentTime = Calendar.getInstance().time
            val formattedTime =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(currentTime)

            // Set the text to currentTimeTextView
            currentTimeTextView.text = "Current Time: $formattedTime\n"

            // Search for start and end stations in the loaded CSV data
            val startStation = searchStation(startStationName)
            val endStation = searchStation(endStationName)

            // Get stop IDs for start and end stations
            val startStopId = startStation?.id ?: "Not found"
            val endStopId = endStation?.id ?: "Not found"

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
                        putStringArrayListExtra("STATION_NAMES", ArrayList(StationList))
                        putStringArrayListExtra("STOP_IDS", ArrayList(ListofStation))
                        putExtra("CURRENT_TIME", formattedTime.split(" ")[1]) // Splitting the formattedTime and taking only the time part
                        putExtra("SINGLE_STATION_NAME", startStationName) // Pass the single station name
                        putExtra("DESTINATION", endStationName) // Pass the single station name
                    }
                    startActivity(intent)
                }
                routesContainer.addView(routeTextView)
            }


//            val routeInfo = findRoute(startStopId, endStopId)
//            Log.e("RouteInformation", routeInfo.toString())
//            if (routeInfo != null) {
//                val routeId = routeInfo.routeId
//                val stopIds = routeInfo.stopIds.joinToString(", ")
//
//                val stationNames = findStationNames(routeInfo.stopIds)
//
//                Log.d("RouteID", routeId ?: "Route not found")
//                Log.d("StopIDs", "Stop IDs: $stopIds")
//                Log.d("StationNames", "Station Names: $stationNames")
//
//                val stationNameToFind = "2264" // Manually specified stop ID
//                val SinglestationName = findStation(stationNameToFind)
//
//                Log.d("FoundStationName", "Station Name for Stop ID $stationNameToFind: $SinglestationName")
//
//                val intent = Intent(this, Activity6::class.java).apply {
//                    putStringArrayListExtra("STATION_NAMES", ArrayList(stationNames))
//                    putStringArrayListExtra("STOP_IDS", ArrayList(routeInfo.stopIds))
//                    putExtra("CURRENT_TIME", formattedTime.split(" ")[1]) // Splitting the formattedTime and taking only the time part
//                    putExtra("SINGLE_STATION_NAME", SinglestationName) // Pass the single station name
//                }
//                startActivity(intent)
//            }
        } catch (e: Exception) {
            // Log the exception
            Log.e("Exception", "An error occurred: ${e.message}", e)
            // You can also display an error message to the user if necessary
            // Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Add this function to dynamically create TextViews for matching routes
    private fun createRouteTextView(route: Triple<String, Pair<String, String>, List<String>>): TextView {
        val textView = TextView(this)
        textView.text = "Route ID: ${route.first}\n" +
                "First Stop Name: ${route.second.first}\n" +
                "Last Stop Name: ${route.second.second}\n\n"
        textView.setTypeface(null, Typeface.BOLD)
        return textView
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

    private fun loadStopsFromCSV(): List<Stop> {
        val stopsList = mutableListOf<Stop>()
        val inputStream = resources.openRawResource(R.raw.stops)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.use { reader ->
            var line: String?
            reader.readLine() // Skip the header line
            while (reader.readLine().also { line = it } != null) {
                val parts = line!!.split(",")
                val stop = Stop(
                    parts[0],
                    parts[1],
                    parts[2].toDouble(),
                    parts[3].toDouble(),
                    parts[4],
                    parts[5]
                )
                stopsList.add(stop)
            }
        }
        return stopsList
    }

    private fun loadRoutesFromCSV(): List<Route> {
        val routesList = mutableListOf<Route>()
        val inputStream = resources.openRawResource(R.raw.output)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.use { reader ->
            var line: String?
            reader.readLine() // Skip the header line
            while (reader.readLine().also { line = it } != null) {
                val parts = line!!.split(",")
                val routeId = parts[0]
                val stopIds = parts.subList(1, parts.size)
                    .joinToString(",") // Join stopIds into a single string
                val route = Route(routeId, stopIds)
                routesList.add(route)
            }
        }
        return routesList
    }

    private fun searchStation(name: String): Stop? {
        return stops.find { it.name == name }
    }
    private fun findStation(id: String): String? {
        val stop = stops.find { it.id == id }
        return stop?.name
    }

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
//    private fun findallRoutes(startStopId: String, endStopId: String): List<RouteInfo> {
//        val matchingRoutes = mutableListOf<RouteInfo>()
//
//        // Loop through each route
//        for (route in routes) {
//            // Extract route ID and stop IDs from the route object
//            val routeId = route.routeId
//            val stopIds = route.stopIds
//
//            // Remove brackets from stopIds and split into individual IDs
//            val cleanedStopIds = stopIds.replace("[", "").replace("]", "").replace(" ", "")
//            val stopIdList = cleanedStopIds.split(",") // Split by comma
//
//            // Convert startStopId and endStopId to integers
//            val startId = startStopId.toInt()
//            val endId = endStopId.toInt()
//
//            // Check if startStopId and endStopId are in stopIds
//            val startIndex = stopIdList.indexOf(startId.toString())
//            val endIndex = stopIdList.indexOf(endId.toString())
//
//            if (startIndex != -1 && endIndex != -1) {
//                // Start and end stop IDs found in route
//                // Check if they are consecutive or in reverse order
//                if (startIndex < endIndex) {
//                    // Found a valid route
//                    val routeInfo = RouteInfo(routeId, stopIdList)
//                    matchingRoutes.add(routeInfo)
//                }
//            }
//        }
//
//        // If no routes found, matchingRoutes will be empty
//        return matchingRoutes
//    }
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
