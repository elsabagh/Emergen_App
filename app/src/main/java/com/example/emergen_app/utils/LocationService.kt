package com.example.emergen_app.utils


import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

@SuppressLint("MissingPermission")
fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    onLocationFetched: (Double, Double) -> Unit
) {
    val locationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = 5000
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                // إرجاع الإحداثيات (Lat, Long) بعد الحصول عليها
                onLocationFetched(location.latitude, location.longitude)
                fusedLocationClient.removeLocationUpdates(this) // Stop updates after getting location
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}


fun checkIfGpsEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun updateLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    onLocationUpdated: (String) -> Unit // تعديل المعامل ليقبل نصًا فقط
) {
    fetchLocation(fusedLocationClient, context) { lat, lon ->
        // تنسيق الإحداثيات في نص واحد
        val location = "$lat,$lon"
        onLocationUpdated(location) // تمرير النص بدلاً من المعاملات المنفصلة
    }
}
