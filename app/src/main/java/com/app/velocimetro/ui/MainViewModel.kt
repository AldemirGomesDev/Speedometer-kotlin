package com.app.velocimetro.ui

import androidx.lifecycle.ViewModel
import com.app.velocimetro.data.repository.MainRepository
import com.google.android.gms.location.LocationRequest

class MainViewModel(
    private val repository: MainRepository,
    private val locationRequest: LocationRequest,
)  : ViewModel()  {

}