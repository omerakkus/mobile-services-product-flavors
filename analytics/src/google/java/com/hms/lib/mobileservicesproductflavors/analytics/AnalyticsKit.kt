package com.hms.lib.mobileservicesproductflavors.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsKit(private val context: Context) : CommonAnalytics {
    override fun saveEvent(key: String, bundle: Bundle) {
        FirebaseAnalytics.getInstance(context)
        Firebase.analytics.logEvent(key,bundle)
    }
}