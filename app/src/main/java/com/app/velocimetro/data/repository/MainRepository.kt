package com.app.velocimetro.data.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    val location: LocationRepository
)
