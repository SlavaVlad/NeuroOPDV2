package com.apu.neuroopdsmart.api.service

import android.util.Log
import com.apu.neuroopdsmart.api.Api
import com.apu.neuroopdsmart.api.RetrofitClient
import com.apu.neuroopdsmart.api.model.Adjective
import com.apu.neuroopdsmart.api.model.Profession
import com.apu.neuroopdsmart.api.model.SurveyProfession
import com.apu.neuroopdsmart.api.model.SurveyResult
import com.apu.neuroopdsmart.api.model.TestResult
import okhttp3.ResponseBody
import java.lang.Exception

class ApiService {
    val TAG = "ApiService"

    private val retrofit = RetrofitClient.getClient()
    private val api: Api = retrofit.create(Api::class.java)

    fun getProfessions(
        userId: String?,
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (List<Profession>) -> Unit,
    ) {
        val call = api.getProfessions(userId)
            .execute()

        if (call.errorBody() != null) {
            onError(call.errorBody()!!)
        }
        onSuccess(call.body()!!)
    }
    fun getAdjectivesByCategory(
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (List<Adjective>) -> Unit,
    ) {
        val call = api.getAdjectivesByCategory()
            .execute()

        if (call.errorBody() != null) {
            onError(call.errorBody()!!)
            return
        }

        onSuccess(call.body()!!)
    }

    fun createProfession(
        profession: Profession,
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (Profession?) -> Unit = {},
    ) {
        try {
            val call = api.addProfession(profession)
                .execute()

            if (call.errorBody() != null) {
                onError(call.errorBody()!!)
            } else {
                onSuccess(call.body())
            }
        } catch (e: Exception) {
            Log.e(TAG, "createProfession: ${e.message}", e)
        }
    }

    fun sendSurveyResult(
        userId: Int,
        surveyResult: SurveyResult,
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (SurveyResult?) -> Unit = {},
    ) {
        val call = api.sendSurveyResult(surveyResult, userId)
            .execute()

        if (call.errorBody() != null) {
            onError(call.errorBody()!!)
        } else {
            onSuccess(call.body())
        }
    }

    fun getSurveyResult(
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (List<SurveyProfession>?) -> Unit = {},
    ) {
        val call = api.getSurveyResults()
            .execute()

        if (call.errorBody() != null) {
            onError(call.errorBody()!!)
        } else {
            onSuccess(call.body())
        }
    }

    fun uploadTestResult(
        userId: Int,
        testResult: TestResult,
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: () -> Unit = {},
    ) {
        val call = api.uploadTestResult(testResult, userId)
            .execute()

        if (!call.isSuccessful) {
            onError(call.errorBody()!!)
        } else {
            call.body()
            onSuccess()
        }
        onSuccess()
    }
}
