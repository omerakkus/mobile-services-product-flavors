package com.hms.lib.mobileservicesproductflavors.push

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.hms.lib.mobileservicesproductflavors.push.model.Provider
import com.hms.lib.mobileservicesproductflavors.push.model.Token

class PushServiceImpl(context: Context): PushService(context) {
    companion object {
        private const val TAG = "GooglePushServiceImpl"
    }

    private val firebaseMessaging = FirebaseMessaging.getInstance()
    override fun autoInitEnabled(enable: Boolean) {
        firebaseMessaging.isAutoInitEnabled = enable
    }

    override fun getToken(): Work<Token> {
        val work: Work<Token> = Work()
        firebaseMessaging.token
            .addOnSuccessListener { work.onSuccess(Token(Provider.Google, it)) }
            .addOnFailureListener { work.onFailure(it) }
            .addOnCanceledListener { work.onCanceled() }
        return work
    }

    override fun subscribeToTopic(topic: String): Work<Unit> {
        val work: Work<Unit> = Work()
        firebaseMessaging.subscribeToTopic(topic)
            .addOnSuccessListener { work.onSuccess(Unit) }
            .addOnFailureListener { work.onFailure(it) }
            .addOnCanceledListener { work.onCanceled() }

        return work
    }

    override fun unsubscribeFromTopic(topic: String): Work<Unit> {
        val work: Work<Unit> = Work()
        firebaseMessaging.unsubscribeFromTopic(topic)
            .addOnSuccessListener { work.onSuccess(Unit) }
            .addOnFailureListener { work.onFailure(it) }
            .addOnCanceledListener { work.onCanceled() }
        return work
    }
}