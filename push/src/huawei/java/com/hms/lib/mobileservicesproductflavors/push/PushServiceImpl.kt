package com.hms.lib.mobileservicesproductflavors.push

import android.content.Context
import com.hms.lib.mobileservicesproductflavors.push.model.Provider
import com.hms.lib.mobileservicesproductflavors.push.model.Token
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging

class PushServiceImpl(private val context: Context): PushService(context) {
    companion object {
        private const val TAG = "HuaweiPushServiceImpl"
    }

    private val hmsMessaging = HmsMessaging.getInstance(context)
    override fun autoInitEnabled(enable: Boolean) {
        hmsMessaging.isAutoInitEnabled = enable
    }

    override fun getToken(): Work<Token> {
        val work: Work<Token> = Work()
        object : Thread() {
            override fun run() {
                try {
                    val appId: String =
                        AGConnectServicesConfig.fromContext(context).getString("client/app_id")
                    val token: String = HmsInstanceId.getInstance(context).getToken(appId, "HCM")
                    work.onSuccess(Token(Provider.Huawei, token))
                } catch (e: ApiException) {
                    work.onFailure(e)
                }
            }
        }.start()

        return work
    }

    override fun subscribeToTopic(topic: String): Work<Unit> {
        val work: Work<Unit> = Work()
        hmsMessaging.subscribe(topic)
            .addOnSuccessListener { work.onSuccess(Unit) }
            .addOnFailureListener { work.onFailure(it) }
            .addOnCanceledListener { work.onCanceled() }
        return work
    }

    override fun unsubscribeFromTopic(topic: String): Work<Unit> {
        val work: Work<Unit> = Work()
        hmsMessaging.unsubscribe(topic)
            .addOnSuccessListener { work.onSuccess(Unit) }
            .addOnFailureListener { work.onFailure(it) }
            .addOnCanceledListener { work.onCanceled() }
        return work
    }
}