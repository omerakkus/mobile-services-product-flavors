package com.hms.lib.mobileservicesproductflavors.push.model

import java.io.Serializable

enum class Provider(val value: String): Serializable {
    Huawei("huawei"),
    Google("google")
}