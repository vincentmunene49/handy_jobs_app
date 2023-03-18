package com.example.professionalapp.viewmodel

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.*
import com.google.firebase.firestore.GeoPoint


fun receiveLocationUpdates(context: Context, onGeoPointReceived: (GeoPoint) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                onGeoPointReceived(geoPoint)
            }else{
                Log.d("NULL","it is null")
            }
        }
    } catch (e: SecurityException) {
        Log.d("LOCATION", "getCurrentLocation: SecurityException: ${e.message}")
    }
}







