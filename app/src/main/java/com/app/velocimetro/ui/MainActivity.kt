package com.app.velocimetro.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.app.velocimetro.R

class MainActivity : AppCompatActivity(), LocationListener {
    private var currentSpeed: TextView? = null
    private var maximumSpeed: TextView? = null
    private var tvDistance: TextView? = null
    private var locationManager: LocationManager? = null
    private var isGPSEnabled = false
    private var _location: Location? = null
    private var maximum = 0f
    private var _distance = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentSpeed = findViewById<View>(R.id.currentSpeed) as TextView
        maximumSpeed = findViewById<View>(R.id.maximumSpeed) as TextView
        tvDistance = findViewById<View>(R.id.distance) as TextView
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        //verificando ou solicitando permissões
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else  // getting GPS status
            isGPSEnabled = locationManager!!
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGPSEnabled) {
            activeGPS(this, getString(R.string.text_active_gps_message))
        } else {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, 0f, this
            )
            if (locationManager != null) {
                _location = locationManager!!
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
        }
        //button close
        findViewById<View>(R.id.close).setOnClickListener {
            dialogClose(
                this@MainActivity,
                getString(R.string.want_to_close)
            )
        }
    }

    private fun getDistance(location: Location, currentLocation: Location) {
        val latitude: Double = location.latitude
        val longitude: Double = location.longitude
        val crntLocation = Location("crntlocation")
        crntLocation.latitude = currentLocation.latitude
        crntLocation.longitude = currentLocation.longitude

        val newLocation = Location("newlocation")
        newLocation.latitude = latitude
        newLocation.longitude = longitude

        _distance += crntLocation.distanceTo(newLocation) / 1000
        tvDistance!!.text = String.format("%.3f", _distance) + " Km"

    }

    override fun onLocationChanged(location: Location) {
        if(_location == null) {
            _location = location
        }
        Log.d(TAG, "onLocationChanged: $location")
        getDistance(_location!!, location)
        _location = location
        try {
            val nCurrentSpeed = location.speed * 3600 / 1000
            val currentSpeed = String.format("%.2f", nCurrentSpeed)
            this.currentSpeed!!.text = currentSpeed
            Log.d(TAG, "km $nCurrentSpeed")
            if (nCurrentSpeed > maximum) {
                maximum = nCurrentSpeed
                maximumSpeed!!.text = currentSpeed
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(TAG, "onProviderDisabled $provider")
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(TAG, "onProviderEnabled $provider")
    }
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d(TAG, "onStatusChanged $provider")
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 102) {
            isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!
            if (!isGPSEnabled) {
                activeGPS(this, getString(R.string.text_active_gps_message))
            } else {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0, 0f, this
                )
                if (locationManager != null) {
                    _location = locationManager!!
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
            }
        }
    }

    //method with dialog active GPS
    private fun activeGPS(context: Context, message: String?) {
        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        window!!.setGravity(Gravity.CENTER)
        dialog.setContentView(R.layout.dialog_gps)
        dialog.setCancelable(false)
        val tvMessage = dialog.findViewById<View>(R.id.texto) as TextView
        tvMessage.text = message
        val llOkClose = dialog.findViewById<View>(R.id.ok_btn) as LinearLayout
        val llOkCancel = dialog.findViewById<View>(R.id.voltar) as ImageView
        llOkClose.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent, 102)
            dialog.dismiss()
        }
        llOkCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    //method with dialog exit application
    private fun dialogClose(context: Context, message: String?) {
        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        window!!.setGravity(Gravity.CENTER)
        dialog.setContentView(R.layout.dialog_close)
        dialog.setCancelable(false)
        val tvMessage = dialog.findViewById<View>(R.id.texto) as TextView
        tvMessage.text = message
        val llOkClose = dialog.findViewById<View>(R.id.ok_btn) as LinearLayout
        val llOkCancel = dialog.findViewById<View>(R.id.voltar) as ImageView
        llOkClose.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        llOkCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    companion object {
        private const val TAG = "Looking_for_Location"
    }
}