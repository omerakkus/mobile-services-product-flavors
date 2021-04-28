package com.hms.lib.mobileservicesproductflavors.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import java.lang.Exception

class ScanKit : IScanKitAPI  {
    override fun performScan(
        activity: Activity,
        scanResultCode:Int
    ) {
        activity.runWithPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE){
            val options = HmsScanAnalyzerOptions.Creator()
                .setHmsScanTypes(HmsScan.ALL_SCAN_TYPE)
                .create()
            ScanUtil.startScan(activity, scanResultCode, options)
        }
    }

    override fun parseScanToTextData(
        callback: (scanToTextResult: ResultData<String>) -> Unit,
        activity: Activity,
        data: Intent
    ) {
        try {
            (data.getParcelableExtra(ScanUtil.RESULT) as? HmsScan)?.let {
                callback.invoke(ResultData.Success(it.getOriginalValue()))
            }
        }
        catch (e:Exception){
            callback.invoke(ResultData.Failed())
        }
    }
}