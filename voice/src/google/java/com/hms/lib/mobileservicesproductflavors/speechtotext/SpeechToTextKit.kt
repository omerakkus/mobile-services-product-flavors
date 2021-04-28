package com.hms.lib.mobileservicesproductflavors.speechtotext

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions

class SpeechToTextKit:ISpeechToTextAPI {
    override fun performSpeechToText(
        activity: Activity,
        recordAudioResultCode: Int,
        languageCode: String,
        hmsApiKey:String
    ) {
        activity.runWithPermissions(Manifest.permission.RECORD_AUDIO) {
            val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            sttIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")

            try {
                activity.startActivityForResult(sttIntent, recordAudioResultCode)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(activity, "Your device does not support STT.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun parseSpeechToTextData(
        callback: (speechToTextResult: ResultData<String>) -> Unit,
        activity: Activity,
        data: Intent,
        resultCode: Int
    ) {
        val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        result?.let {
            val recognizedText = it[0]
            callback.invoke(ResultData.Success(recognizedText))
        }
    }
}