package com.hms.lib.mobileservicesproductflavors.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.hms.lib.mobileservicesproductflavors.location.model.CheckGpsEnabledResult
import com.hms.lib.mobileservicesproductflavors.location.model.EnableGPSFinalResult
import com.hms.lib.mobileservicesproductflavors.location.model.LocationResult
import com.hms.lib.mobileservicesproductflavors.location.model.Priority
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest

abstract class LocationClient(
    private val activity: Activity,
    lifecycle: Lifecycle,
    private val needBackgroundPermissions: Boolean
) {
    var preferredUnsubscribeEvent = Lifecycle.Event.ON_DESTROY

    init {
        lifecycle.addObserver(object : LifecycleObserver{
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun pause(){
                if(preferredUnsubscribeEvent == Lifecycle.Event.ON_PAUSE){
                    removeLocationUpdates()
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun stop(){
                if(preferredUnsubscribeEvent == Lifecycle.Event.ON_STOP){
                    removeLocationUpdates()
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun destroy(){
                if(preferredUnsubscribeEvent == Lifecycle.Event.ON_DESTROY){
                    removeLocationUpdates()
                }
            }
        })
    }

    var handlePermanentlyDeniedBlock: ((QuickPermissionsRequest) -> Unit)? = null
    var permissionsDeniedBlock: ((QuickPermissionsRequest) -> Unit)? = null

    var options = QuickPermissionsOptions().apply {
        handleRationale=false
        handlePermanentlyDenied=true
        permanentDeniedMethod = {
            handlePermanentlyDeniedBlock?.invoke(it)
        }
        permissionsDeniedMethod ={
            permissionsDeniedBlock?.invoke(it)
        }
    }

    private var enableGpsCallback : ((enableGPSFinalResult: EnableGPSFinalResult, error: Exception?) -> Unit)? = null

    fun enableGps(callback : (enableGPSFinalResult: EnableGPSFinalResult,
                              error: Exception?) -> Unit){

        activity.runWithPermissions(*getLocationPermissions(),options = options){
            enableGpsCallback=callback
            checkLocationSettings(activity){checkGpsEnabledResult, error ->
                when(checkGpsEnabledResult){
                    CheckGpsEnabledResult.ENABLED-> callback.invoke(EnableGPSFinalResult.ENABLED,null)
                    CheckGpsEnabledResult.ERROR -> callback.invoke(EnableGPSFinalResult.FAILED,error)
                }
            }
        }
    }

    abstract fun checkLocationSettings(activity: Activity,callback: (checkGpsEnabledResult: CheckGpsEnabledResult,
                                                                     error: Exception?) -> Unit)

    fun handleResolutionResult(resultCode: Int){
        when(resultCode){
            Activity.RESULT_OK ->             // All required changes were successfully made
                enableGpsCallback?.invoke(EnableGPSFinalResult.ENABLED,null)
            Activity.RESULT_CANCELED ->             // The user was asked to change settings, but chose not to
                enableGpsCallback?.invoke(EnableGPSFinalResult.USER_CANCELLED,null)
        }
    }

    fun getLastKnownLocation(locationListener: (LocationResult) -> Unit) =
        activity.runWithPermissions(*getLocationPermissions(),options = options){
            getLastKnownLocationCore(locationListener)
        }

    abstract fun getLastKnownLocationCore(locationListener: (LocationResult) -> Unit)

    fun requestLocationUpdates(
        priority: Priority?= Priority.PRIORITY_BALANCED_POWER_ACCURACY,
        interval: Long?=10000,
        locationListener: (LocationResult) -> Unit
    ) = activity.runWithPermissions(*getLocationPermissions(),options = options){
        requestLocationUpdatesCore(priority,interval,locationListener)
    }

    abstract fun requestLocationUpdatesCore(priority: Priority?= Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                                            interval: Long?=10000,
                                            locationListener: (LocationResult) -> Unit)

    abstract fun removeLocationUpdates()

    protected fun getLocationPermissions() : Array<String>{
        val perms= mutableListOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P && needBackgroundPermissions)
            perms.add("android.permission.ACCESS_BACKGROUND_LOCATION")
        return perms.toTypedArray()
    }

    fun isLocationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            val mode = Settings.Secure.getInt(
                activity.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }
}