package com.apu.neuroopdsmart.api.service

import android.util.Log
import okhttp3.ResponseBody

const val TAG = "NetworkCallback"

fun logResponse(resp: ResponseBody) {
    Log.e(TAG, "logError: ${resp.string()}")
}
