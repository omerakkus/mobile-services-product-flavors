package com.hms.lib.mobileservicesproductflavors.push.model

import android.net.Uri
import java.io.Serializable

data class PushMessage(
    val data: Map<String, String>? = null,
    val senderId: String? = null,
    val from: String? = null,
    val to: String? = null,
    val collapseKey: String? = null,
    val messageId: String? = null,
    val messageType: String? = null,
    val sentTime: Long = 0,
    val ttl: Int = 0,
    val originalPriority: Int = 0,
    val priority: Int = 0,
    val notification: Notification
) : Serializable {
    companion object {
        const val PRIORITY_UNKNOWN = 0
        const val PRIORITY_HIGH = 1
        const val PRIORITY_NORMAL = 2
    }

    data class Notification(
        val title: String? = null,
        val titleLocalizationKey: String? = null,
        val titleLocalizationArgs: Array<String>? = null,
        val body: String? = null,
        val bodyLocalizationKey: String? = null,
        val bodyLocalizationArgs: Array<String>? = null,
        val icon: String? = null,
        val imageUrl: Uri? = null,
        val sound: String? = null,
        val tag: String? = null,
        val color: String? = null,
        val clickAction: String? = null,
        val channelId: String? = null,
        val link: Uri? = null,
        val ticker: String? = null,
        val notificationPriority: Int? = null,
        val visibility: Int? = null,
        val notificationCount: Int? = null,
        val lightSettings: IntArray? = null,
        val eventTime: Long? = null,
        val sticky: Boolean = false,
        val localOnly: Boolean = false,
        val defaultSound: Boolean = false,
        val defaultVibrateSettings: Boolean = false,
        val defaultLightSettings: Boolean = false,
        val vibrateTimings: LongArray? = null
    ) : Serializable
}