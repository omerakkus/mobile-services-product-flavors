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
package com.hms.lib.mobileservicesproductflavors.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.hms.lib.mobileservicesproductflavors.map.factory.BaseMapImpl
import com.hms.lib.mobileservicesproductflavors.map.factory.CommonMap
import com.hms.lib.mobileservicesproductflavors.map.model.*
import com.hms.lib.mobileservicesproductflavors.map.util.Util.isPackageInstalled
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions

class MapClientImpl(context: Context, apiKey: String? = null) : BaseMapImpl(context),
    CommonMap {

    private var mapView: MapView = MapView(context)

    private lateinit var map: HuaweiMap

    init {
        apiKey?.let {
            MapsInitializer.setApiKey(it)
        }
    }

    override fun getMapView(): View {
        return mapView
    }

    override fun onCreate(bundle: Bundle?) {
        mapView.onCreate(bundle)
    }

    override fun getMapAsync(onMapReadyListener: (map: CommonMap) -> Unit) {
        mapView.getMapAsync {
            map = it
            onMapReadyListener.invoke(this)
        }
    }

    override fun addPolygon(commonPolygonOptions: CommonPolygonOptions): MapPolygon {
        return map.addPolygon(commonPolygonOptions.toHMSPolygonOptions()).toCommonPolygon()
    }

    override fun addMarker(
        title: String?,
        snippet: String?,
        latitude: Double,
        longitude: Double,
        iconBitmap: Bitmap?,
        anchor: Pair<Float, Float>?
    ): MapMarker {
        val markerOptions = MarkerOptions().apply {
            position(LatLng(latitude, longitude))
            title?.let { title(it) }
            snippet?.let { snippet(it) }
            iconBitmap?.let { icon(BitmapDescriptorFactory.fromBitmap(it)) }
            anchor?.let { anchor(it.first, it.second) }
        }
        return MapMarker(map.addMarker(markerOptions))
    }

    override fun setOnInfoWindowClickListener(
        markerClickCallback: (
            markerTitle: String?,
            markerSnippet: String?,
            commonLatLng: CommonLatLng
        ) -> Unit
    ) {
        map.setOnInfoWindowClickListener { marker ->
            markerClickCallback.invoke(
                marker.title,
                marker.snippet,
                marker.position.toCommonLatLng()
            )
        }
    }

    override fun setOnMapClickListener(onClick: (commonLatLng: CommonLatLng) -> Unit) {
        map.setOnMapClickListener {
            onClick.invoke(CommonLatLng(it.latitude, it.longitude))
        }
    }

    override fun moveCamera(latitude: Double, longitude: Double, zoomRatio: Float) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoomRatio)
        )
    }

    override fun animateCamera(latitude: Double, longitude: Double, zoomRatio: Float) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoomRatio)
        )
    }

    override fun setMyLocationEnabled(myLocationEnabled: Boolean?, context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        map.isMyLocationEnabled = myLocationEnabled!!
        return true
    }

    override fun clear() {
        map.clear()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        mapView.onSaveInstanceState(bundle)
    }

    override fun onStart() {
        mapView.onStart()
    }

    override fun onResume() {
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
    }

    override fun onStop() {
        mapView.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
    }

    override fun getCameraPosition(): CommonCameraPosition {
        println("huawei map: $map")
        println("camera position huawei: ${map.cameraPosition.target}")
        return CommonCameraPosition(
            CommonLatLng(map.cameraPosition.target.latitude, map.cameraPosition.target.longitude),
            map.cameraPosition.zoom,
            map.cameraPosition.tilt,
            map.cameraPosition.bearing
        )
    }

    override fun setOnCameraIdleListener(listener: () -> Unit) {
        map.setOnCameraIdleListener { listener.invoke() }
    }

    override fun setOnCameraMoveStartedListener(listener: () -> Unit) {
        map.setOnCameraMoveStartedListener { listener.invoke() }
    }

    override fun createRouteToTheSelectedLocation(
        context: Context,
        commonLatLng: CommonLatLng
    ): OpenNavigationResult {
        try {
            val intentUri =
                when {
                    isPackageInstalled(context,"com.huawei.maps.app") ->
                        Uri.parse("mapapp://navigation?daddr=${commonLatLng.lat},${commonLatLng.lng}")
                    isPackageInstalled(context,"ru.yandex.yandexmaps") ->
                        Uri.parse("yandexmaps://maps.yandex.ru/?pt=${commonLatLng.lng},${commonLatLng.lat}&z=12&l=map")
                    else ->
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=${commonLatLng.lat},${commonLatLng.lng}")
                }

            val intent = Intent(Intent.ACTION_VIEW, intentUri)
            context.startActivity(intent)
            return OpenNavigationResult()
        } catch (e: Exception) {
            return OpenNavigationResult(false, e)
        }
    }


    override fun isCompassEnabled(): Boolean {
        return map.uiSettings.isCompassEnabled
    }

    override fun setCompassEnabled(compassEnabled: Boolean?) {
        map.uiSettings.isCompassEnabled = compassEnabled!!
    }

    override fun isIndoorLevelPickerEnabled(): Boolean {
        return map.uiSettings.isIndoorLevelPickerEnabled

    }

    override fun setIndoorLevelPickerEnabled(indoorLevelPickerEnabled: Boolean?) {
        map.uiSettings.isIndoorLevelPickerEnabled = indoorLevelPickerEnabled!!
    }

    override fun isMapToolbarEnabled(): Boolean {
        return map.uiSettings.isMapToolbarEnabled
    }

    override fun setMapToolbarEnabled(mapToolbarEnabled: Boolean?) {
        map.uiSettings.isMapToolbarEnabled = mapToolbarEnabled!!
    }

    override fun isMyLocationButtonEnabled(): Boolean {
        return map.uiSettings.isMyLocationButtonEnabled
    }

    override fun setMyLocationButtonEnabled(myLocationButtonEnabled: Boolean?) {
        map.uiSettings.isMyLocationButtonEnabled = myLocationButtonEnabled!!
    }

    override fun isRotateGesturesEnabled(): Boolean {
        return map.uiSettings.isRotateGesturesEnabled
    }

    override fun setRotateGesturesEnabled(rotateGesturesEnabled: Boolean?) {
        map.uiSettings.isRotateGesturesEnabled = rotateGesturesEnabled!!
    }

    override fun isScrollGesturesEnabled(): Boolean {
        return map.uiSettings.isScrollGesturesEnabled
    }

    override fun setScrollGesturesEnabled(scrollGesturesEnabled: Boolean?) {
        map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = scrollGesturesEnabled!!
    }

    override fun isScrollGesturesEnabledDuringRotateOrZoom(): Boolean {
        return map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom
    }

    override fun setScrollGesturesEnabledDuringRotateOrZoom(scrollGesturesEnabledDuringRotateOrZoom: Boolean?) {
        map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom =
            scrollGesturesEnabledDuringRotateOrZoom!!
    }

    override fun isTiltGesturesEnabled(): Boolean {
        return map.uiSettings.isTiltGesturesEnabled
    }

    override fun setTiltGesturesEnabled(tiltGesturesEnabled: Boolean?) {
        map.uiSettings.isTiltGesturesEnabled = tiltGesturesEnabled!!
    }

    override fun isZoomControlsEnabled(): Boolean {
        return map.uiSettings.isZoomControlsEnabled
    }

    override fun setZoomControlsEnabled(zoomControlsEnabled: Boolean?) {
        map.uiSettings.isZoomControlsEnabled = zoomControlsEnabled!!
    }

    override fun isZoomGesturesEnabled(): Boolean {
        return map.uiSettings.isZoomGesturesEnabled
    }

    override fun setZoomGesturesEnabled(zoomGesturesEnabled: Boolean?) {
        map.uiSettings.isZoomGesturesEnabled = true
    }

    override fun setAllGesturesEnabled(allGestureEnable: Boolean?) {
        map.uiSettings.setAllGesturesEnabled(allGestureEnable!!)
    }

}
