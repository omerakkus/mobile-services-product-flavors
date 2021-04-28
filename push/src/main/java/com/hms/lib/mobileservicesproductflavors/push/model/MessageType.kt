package com.hms.lib.mobileservicesproductflavors.push.model

import java.io.Serializable

enum class MessageType(val value: Int): Serializable {
    MessageReceived(0),
    DeletedMessages(1),
    MessageSent(2),
    SendError(3),
    NewToken(4),
}