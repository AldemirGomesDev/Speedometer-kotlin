package com.app.velocimetro.data.repository

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import com.app.velocimetro.data.persistence.Database
import com.app.velocimetro.util.checkLocationPermission
import com.app.velocimetro.util.isGPSEnabled
import com.google.android.gms.location.LocationServices
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val application: Application,
    private val database: Database
) {

    @SuppressLint("MissingPermission")
    fun getLocation() {
        /*
         * One time location request
         */
        if (application.isGPSEnabled() && application.checkLocationPermission()) {
            LocationServices.getFusedLocationProviderClient(application)
                ?.lastLocation
                ?.addOnSuccessListener { location: android.location.Location? ->
                    if (location != null)
                        saveLocation(location)
                }
        }
    }

    private fun saveLocation(location: Location) = GlobalScope.launch { database.locationDao().insert(location) }

    fun getSavedLocation(): Flowable<List<Location>> = database.locationDao().selectAll()
}