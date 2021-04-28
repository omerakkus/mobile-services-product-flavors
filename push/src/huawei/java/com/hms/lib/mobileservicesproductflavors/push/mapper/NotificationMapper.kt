package com.hms.lib.commonmobileservices.push.model.mapper

import com.hms.lib.mobileservicesproductflavors.push.model.Mapper
import com.hms.lib.mobileservicesproductflavors.push.model.PushMessage
import com.huawei.hms.push.RemoteMessage

class NotificationMapper: Mapper<RemoteMessage.Notification, PushMessage.Notification>() {
    override fun map(from: RemoteMessage.Notification): PushMessage.Notification =
        PushMessage.Notification(
            title = from.title,
            titleLocalizationKey = from.titleLocalizationKey,
            titleLocalizationArgs = from.titleLocalizationArgs,
            body = from.body,
            bodyLocalizationKey = from.bodyLocalizationKey,
            bodyLocalizationArgs = from.bodyLocalizationArgs,
            icon = from.icon,
            imageUrl = from.imageUrl,
            sound = from.sound,
            tag = from.tag,
            color = from.color,
            clickAction = from.clickAction,
            channelId = from.channelId,
            link = from.link,
            ticker = from.ticker,
            notificationPriority = from.importance,
            visibility = from.visibility,
            lightSettings = from.lightSettings,
            localOnly = from.isLocalOnly,
            defaultSound = from.isDefaultSound,
            vibrateTimings = from.vibrateConfig
        )
}