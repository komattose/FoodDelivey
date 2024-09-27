package com.example.myapplication6.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.firebase.database.FirebaseDatabase

object LocationUtil {
    private fun getLastKnownLocation(context: Context): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }

    fun saveUserLocation(userId: String, context: Context) {
        val location = getLastKnownLocation(context)
        location?.let {
            val userLocation = mapOf("latitude" to it.latitude, "longitude" to it.longitude)
            val database = FirebaseDatabase.getInstance().reference
            database.child("users").child(userId).child("location").setValue(userLocation)
        }
    }
}
