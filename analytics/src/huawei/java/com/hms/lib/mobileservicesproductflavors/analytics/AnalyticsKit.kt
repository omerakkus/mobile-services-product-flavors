package com.hms.lib.mobileservicesproductflavors.analytics
import android.content.Context
import android.os.Bundle
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsTools

class AnalyticsKit(context: Context) : CommonAnalytics {
    private val hmsAnalytics =  HiAnalytics.getInstance(context)
    init {
        HiAnalyticsTools.enableLog()
    }
    override fun saveEvent(key: String, bundle: Bundle) {
        hmsAnalytics.onEvent(key,bundle)
    }
}