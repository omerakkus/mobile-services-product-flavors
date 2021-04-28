## Mobile Services Product Flavors

### Product Flavor
Product flavor is a variant of your app. It is very useful when you want to create multiple versions of your app. This means you can generate different versions or variants of your app using a single codebase.
Product flavors are a powerful feature of the Gradle plugin from Android Studio to create customised versions of products. They form part of what we call Build Variants.

### Build Variants
Build Variants are formed by Build Types and Product Flavors.
According to the Google documentation, build variants are the result of Gradle using a specific set of rules to combine settings, code, and resources configured in your build types and product flavors.
Build Type applies different build and packaging settings. An example of build types are “Debug” and “Release”.
Product Flavors specify different features and device requirements, such as custom source code, resources, and minimum API levels.

### Why do we use product flavor when integrating HMS?
This method is used as follows to build the `HMS` and `GMS` versions of the application separately.

Currently added services: `map`, `location`, `scan`, `voice`, `analytics` and `push`.

### Getting Started
**You need to agconnect-services.json and google-services.json to run this project correctly.**

- Create and configure project on AppGallery Connect and Firebase Console
- Open your Android project and find Debug FingerPrint (SHA256) with follow this steps; View -> Tool Windows -> Gradle -> Tasks -> Android -> signingReport
- Add SHA256 FingerPrint into your app
- Download agconnect-services.json from AppGallery Connect and download google-services.json from Firebase Console.
- Move to agconnect.json file in base "app/huawei" folder that under your android project.
- Move to agconnect.json file in base "app/google" folder that under your android project.
- Go to app level gradle file and change application id of your android project. It must be same with app id on AppGallery console you defined.


### How to Use
1- The following code has been added to the `app gradle` module. The code snippet below should be added to every module that uses huawei and google product flavors in the project.
```gradle
    flavorDimensions "mobileService"
    productFlavors{
        huawei{
            dimension = "mobileService"
            applicationId = "com.hms.lib.mobileservicesproductflavors.huawei"
        }
        google{
            dimension = "mobileService"
            applicationId = "com.hms.lib.mobileservicesproductflavors"
        }
    }
```
* The dimension is named mobile service, this name `must be the same` in both product flavors.
* Application id `must be unique` for these two products flavor. In this example, ".huawei" has been added to the application id of the product flavor for huawei services.

2- `HuaweiDebug`-`HuaweiRelease`-`GoogleDebug`-`GoogleRelease` options will appear in the Build Variant section as shown in the image below.

![Build Variants](/uploads/30e53f64468de2ca5a83a751d480b46b/sss1.PNG)


3- A new module named a new `map` has been created to make an example. 
* Two packages were created, namely `huawei` and `google`. Under these, a sub-package was created like the `same path with the application id` created in the app gradle file. In the picture below, the Huawei build variant is active. When the files are examined, you can see that the color of the java file under the huawei package is blue. This means huawei build variant is active at the moment.

<img src="/uploads/0b75455349d2bef0880b26afc8e4d25d/sss2.PNG" width="550" height="600">

4- Two packages named huawei and google were created in the `app module` of the project. Below these are the `google-services.json` file required for google services and the `agconnect-services.json` file required for huawei services. In this way, whichever service will be used, the corresponding json file will be used. You can see the example usage in the image below.

<img src="/uploads/cd3c5f727604a5feb6819cc31b8457c7/sss3.PNG" width="559" height="422">

5- There are lines of code that must be added to use Google and Huawei services. Google and huawei control the product flavor, preventing unnecessary libraries from loading as below.

* project gradle file;
```gradle
if(getGradle().getStartParameter().getTaskRequests().toString().toLowerCase().contains("google")) {
    classpath 'com.google.gms:google-services:4.3.5'
        }
else{
    classpath 'com.huawei.agconnect:agcp:1.4.2.301'
}
```

* app gradle file;
```gradle
if(getGradle().getStartParameter().getTaskRequests().toString().toLowerCase().contains("google")){
    apply plugin: 'com.google.gms.google-services'
}else{
    apply plugin: 'com.huawei.agconnect'
}
```

* map gradle file;
```gradle
huaweiImplementation 'com.huawei.hms:maps:5.0.0.300'
googleImplementation 'com.google.android.gms:play-services-maps:17.0.0'
googleImplementation 'com.google.maps.android:android-maps-utils:0.6.2'
```

* location gradle file;
```gradle
huaweiImplementation 'com.huawei.hms:location:4.0.4.300'
googleImplementation 'com.google.android.gms:play-services-location:18.0.0'
```

* scan gradle file;
```gradle
googleImplementation 'com.google.android.gms:play-services-mlkit-barcode-scanning:16.1.4'
huaweiImplementation 'com.huawei.hms:scan:1.2.5.300'
```

* voice gradle file;
```gradle
huaweiImplementation "com.huawei.hms:ml-computer-voice-asr-plugin:2.0.0.300"
```

* analytics gradle file;
```gradle
huaweiImplementation 'com.huawei.hms:hianalytics:5.2.0.300'
googleImplementation 'com.google.firebase:firebase-analytics-ktx:18.0.3'
```

* push gradle file;
```gradle
huaweiImplementation 'com.huawei.hms:push:5.1.1.301'
googleImplementation 'com.google.firebase:firebase-messaging-ktx:21.0.1'
```

6- Common codes to be used by both google and huawei product flavor were used in the main package. Parts where Huawei or Google services were used were used under their own packages. In the image below, you can see the code structure used in common and the code structure used for huawei product flavor.

<img src="/uploads/650f0968e75f271f19142f275a5cea75/sss5.PNG" width="546" height="630">

The classes used separately in the `Huawei` and `Google` product flavor structure `must have the same name`. Below you can see the example of `MapMarker` class for google and huawei product variant.

MapMarker for `Huawei` product flavor;

```kt
import com.huawei.hms.maps.model.Marker

class MapMarker(private val markerImpl: Marker) {
    fun hide() {
        markerImpl.isVisible = false
    }

    fun show() {
        markerImpl.isVisible = true
    }

    fun remove(): Boolean {
        return try {
            markerImpl.remove()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
```

MapMarker for `Google` product flavor;

```kt
import com.google.android.gms.maps.model.Marker
import java.lang.Exception

class MapMarker(private val markerImpl : Marker) {
    fun hide(){
        markerImpl.isVisible=false
    }

    fun show(){
        markerImpl.isVisible=true
    }

    fun remove() : Boolean{
        return try {
            markerImpl.remove()
            true
        }
        catch (e:Exception){
            e.printStackTrace()
            false
        }
    }
}
```

## Summary

### Analytics
```kt
val locationEvent = Bundle().apply {
    putString("custom_location_lat", it.location?.latitude.toString())
    putString("custom_location_lng", it.location?.longitude.toString())
}
AnalyticsKit(this).saveEvent("custom_location", locationEvent)
```

### Location
```kt
LocationClientImpl(this, this.lifecycle, true).also {
    it.enableGps { enableGPSFinalResult, error ->
        when (enableGPSFinalResult) {
            EnableGPSFinalResult.ENABLED -> {
                Log.e(TAG, "location data: ${it}")
            }
            EnableGPSFinalResult.FAILED -> {
                Log.e(TAG, "location failed: ${error?.message}")
            }
            EnableGPSFinalResult.USER_CANCELLED -> {
            }
        }
    }
}
```

### Map
```kt
commonMap = mapView.onCreate(savedInstanceState, lifecycle).apply {
    getMapAsync {
        //Todo: Codes to write when the map is opened.
    }
}
```

### Push
```kt
PushServiceImpl(this).subscribeToTopic("notificationTopic")
PushServiceImpl(this).unsubscribeFromTopic("notificationTopic")
```

### Scan
First of all ScanKit should be perform.
```kt
ScanKit().performScan(this, scanResultCode) 
```

The result should be taken in onActivityResult.
```kt
if (requestCode == scanResultCode) {
    if (data != null) {
        ScanKit().parseScanToTextData({
            it.handleSuccess { resultData ->
                Log.d("ScanSDK", resultData.data.toString())
            }
        }, this, data)
    }
}
```

### Voice
First of all ScanKit should be perform.
```kt
 SpeechToTextKit().performSpeechToText(this, speechToTextResultCode,"en-US", HMS_API_KEY)
```
The result should be taken in onActivityResult.
```kt
if (requestCode == speechToTextResultCode) {
    if (data != null) {
        SpeechToTextKit().parseSpeechToTextData({
            it.handleSuccess {
                Log.d("Speech to text result: ", it.data.toString())
             }
        }, this, data, resultCode)
    }
} 
```