package com.hms.lib.mobileservicesproductflavors.model

import com.hms.lib.mobileservicesproductflavors.map.model.CommonLatLng

data class City(
    val id: String,
    val coordinate: CommonLatLng,
    val name: String
)