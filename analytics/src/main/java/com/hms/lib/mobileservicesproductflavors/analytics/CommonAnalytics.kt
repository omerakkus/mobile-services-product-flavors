package com.hms.lib.mobileservicesproductflavors.analytics

import android.os.Bundle

interface CommonAnalytics {
    fun saveEvent(key:String,bundle: Bundle)
}