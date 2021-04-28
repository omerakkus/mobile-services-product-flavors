package com.hms.lib.mobileservicesproductflavors.map.util

import android.content.Context
import android.content.pm.PackageManager

object Util {
    fun isPackageInstalled(context:Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}