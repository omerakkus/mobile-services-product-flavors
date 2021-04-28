// Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.lib.mobileservicesproductflavors.map.factory

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.hms.lib.mobileservicesproductflavors.map.MapClientImpl
import com.hms.lib.mobileservicesproductflavors.map.model.*

interface CommonMap : UISettings {
    fun getMapView(): View
    fun onCreate(bundle: Bundle?)
    fun getMapAsync(onMapReadyListener: (map: CommonMap) -> Unit)
    fun addMarker(
        title: String? = null,
        snippet: String? = null,
        latitude: Double,
        longitude: Double,
        iconBitmap: Bitmap? = null,
        anchor: Pair<Float, Float>? = null
    ): MapMarker

    fun addPolygon(commonPolygonOptions: CommonPolygonOptions): MapPolygon
    fun setOnInfoWindowClickListener(
        markerClickCallback: (
            markerTitle: String?,
            markerSnippet: String?,
            commonLatLng: CommonLatLng
        ) -> Unit
    )

    fun setOnMapClickListener(onClick: (commonLatLng: CommonLatLng) -> Unit)
    fun moveCamera(latitude: Double, longitude: Double, zoomRatio: Float)
    fun animateCamera(latitude: Double, longitude: Double, zoomRatio: Float)
    fun setMyLocationEnabled(myLocationEnabled: Boolean?, context: Context): Boolean
    fun clear()
    fun onSaveInstanceState(bundle: Bundle)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
    fun onLowMemory()
    fun calculateDistanceBetweenPoints(p1: CommonLatLng, p2: CommonLatLng): Double
    fun getCameraPosition(): CommonCameraPosition
    fun setOnCameraIdleListener(listener: () -> Unit)
    fun setOnCameraMoveStartedListener(listener: () -> Unit)
    fun createRouteToTheSelectedLocation(context: Context, commonLatLng: CommonLatLng): OpenNavigationResult

}