package com.example.emergen_app.utils

import kotlin.math.pow

object CalculateDistance {

    // Constants for the Haversine formula
    const val EARTH_RADIUS_KM = 6371.0  // Radius of the Earth in km

    // Function to calculate distance using Haversine formula
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        val dlat = lat2Rad - lat1Rad
        val dlon = lon2Rad - lon1Rad

        val a = Math.sin(dlat / 2).pow(2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dlon / 2).pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return EARTH_RADIUS_KM * c  // Distance in kilometers
    }

    // Calculate the time based on average speed (e.g., 60 km/h)
    fun calculateTravelTime(distanceKm: Double, avgSpeedKmPerHour: Double): Double {
        return distanceKm / avgSpeedKmPerHour  // Time in hours
    }

    // Example usage
    val lat1 = 30.033  // Latitude for addressBranch
    val lon1 = 31.233  // Longitude for addressBranch
    val lat2 = 30.034  // Latitude for addressMaps
    val lon2 = 31.235  // Longitude for addressMaps

    val distance = calculateDistance(lat1, lon1, lat2, lon2)
    val travelTime = calculateTravelTime(distance, 60.0)  // Assuming an average speed of 60 km/h

}