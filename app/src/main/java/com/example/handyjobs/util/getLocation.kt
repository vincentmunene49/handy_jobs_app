package com.example.handyjobs.util

import android.app.Activity
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object getLocation {

    suspend fun getLocation(activity: Activity): Location? = withContext(Dispatchers.IO) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        return@withContext try {
            fusedLocationProviderClient.lastLocation.await()
        } catch (e: Exception) {
            Log.d("getLocation", "Error getting location: ${e.message}")
            null
        }
    }
}
