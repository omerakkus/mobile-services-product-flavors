package com.hms.lib.mobileservicesproductflavors.push.model

import com.hms.lib.mobileservicesproductflavors.push.model.Provider
import java.io.Serializable

data class Token(
    val provider: Provider,
    val token: String
): Serializable
