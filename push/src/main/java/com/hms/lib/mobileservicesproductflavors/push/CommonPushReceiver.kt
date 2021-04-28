package com.hms.lib.mobileservicesproductflavors.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hms.lib.mobileservicesproductflavors.push.model.MessageType
import com.hms.lib.mobileservicesproductflavors.push.model.PushMessage
import com.hms.lib.mobileservicesproductflavors.push.model.Token

private const val TAG = "CommonPushReceiver"
open class CommonPushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action.equals("com.hms.lib.mobileservicesproductflavors.push.PushService.action.MESSAGING_EVENT")) {
                it.extras?.let { bundle ->
                    val type = bundle["message_type"]
                    val data: Bundle = bundle["data"] as Bundle
                    when (type) {
                        MessageType.NewToken.value -> {
                            onNewToken(data["token"] as Token)
                        }
                        MessageType.MessageReceived.value -> {
                            onMessageReceived(data["message"] as PushMessage)
                        }
                        MessageType.DeletedMessages.value -> {
                            onDeletedMessages()
                        }
                        MessageType.MessageSent.value -> {
                            onMessageSent(data.getString("message_id")!!)
                        }
                        MessageType.SendError.value -> {
                            onSendError(
                                data.getString("message_id")!!,
                                data.getSerializable("exception") as Exception
                            )
                        }
                    }
                }
            }
        }
    }

    open fun onMessageReceived(remoteMessage: PushMessage) {
    }

    open fun onDeletedMessages() {
    }

    open fun onMessageSent(messageId: String) {
    }

    open fun onSendError(messageId: String, exception: Exception) {
    }

    open fun onNewToken(token: Token) {
    }
}