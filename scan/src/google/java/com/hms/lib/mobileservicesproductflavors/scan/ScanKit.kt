package com.hms.lib.mobileservicesproductflavors.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions

class ScanKit : IScanKitAPI {
    override fun performScan(activity: Activity, scanResultCode: Int) {
        activity.runWithPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            activity.startActivityForResult(
                Intent(
                    activity,
                    GoogleBarcodeScannerActivity::class.java
                ), scanResultCode
            )
        }
    }

    override fun parseScanToTextData(
        callback: (scanToTextResult: ResultData<String>) -> Unit,
        activity: Activity,
        data: Intent
    ) {
        try {
            val returnValue: String = data.getStringExtra("scan_result") as String
            callback.invoke(ResultData.Success(returnValue))
        } catch (e: Exception) {
            callback.invoke(ResultData.Failed())
        }
    }
}