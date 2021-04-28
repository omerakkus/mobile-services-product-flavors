package com.hms.lib.commonmobileservices.push.model.mapper

import com.hms.lib.mobileservicesproductflavors.push.model.Mapper
import com.hms.lib.mobileservicesproductflavors.push.model.PushMessage
import com.huawei.hms.push.RemoteMessage

class MessageMapper: Mapper<RemoteMessage, PushMessage>() {

    private val notificationMapper: NotificationMapper = NotificationMapper()

    override fun map(from: RemoteMessage): PushMessage =
        PushMessage(
            data = from.dataOfMap,
            from = from.from,
            to = from.to,
            collapseKey = from.collapseKey,
            messageId = from.messageId,
            messageType = from.messageType,
            sentTime = from.sentTime,
            ttl = from.ttl,
            originalPriority = from.originalUrgency,
            priority = from.urgency,
            notification = notificationMapper.map(from.notification!!)
        )
}