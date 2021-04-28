package com.hms.lib.mobileservicesproductflavors.push.model

abstract class Mapper<From, To> {
    abstract fun map(from: From): To
}