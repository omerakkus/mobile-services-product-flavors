package com.hms.lib.mobileservicesproductflavors.push

import android.content.Context
import com.hms.lib.mobileservicesproductflavors.push.model.Token

private lateinit var INSTANCE: PushService

abstract class PushService(private val context: Context) {
    fun initialize(autoInitEnabled: Boolean = false) {
        autoInitEnabled(autoInitEnabled)
    }

    abstract fun autoInitEnabled(enable: Boolean)
    abstract fun getToken(): Work<Token>
    abstract fun subscribeToTopic(topic: String): Work<Unit>
    abstract fun unsubscribeFromTopic(topic: String): Work<Unit>

    companion object {
        fun getInstance(context: Context): PushService {
            synchronized(PushKitService::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = PushServiceImpl(context)
                }
            }
            return INSTANCE
        }
    }
}