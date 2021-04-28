package com.hms.lib.mobileservicesproductflavors.location.model

import android.location.Location
import java.lang.Exception

data class LocationResult(val location: Location?=null,
                                val state: LocationResultState = LocationResultState.SUCCESS,
                                val exception: Exception?=null)