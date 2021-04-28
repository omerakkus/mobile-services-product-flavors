package com.hms.lib.mobileservicesproductflavors.push

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hms.lib.mobileservicesproductflavors.push.model.MessageType
import java.lang.Exception

object BroadcastHelper {
    fun sendMessage(context: Context, messageType: MessageType, bundle: Bundle? = null) {
        val intent = Intent("com.hms.lib.mobileservicesproductflavors.action.MESSAGING_EVENT")
        intent.putExtra("data", bundle)
        intent.putExtra("message_type", messageType.value)
        intent.setPackage(context.packageName)
        context.sendBroadcast(intent)
    }
}