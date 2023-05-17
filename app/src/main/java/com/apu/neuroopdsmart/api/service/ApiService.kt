package com.apu.neuroopdsmart.api.service

import android.util.Log
import com.apu.neuroopdsmart.api.Api
import com.apu.neuroopdsmart.api.RetrofitClient
import com.apu.neuroopdsmart.api.model.Adjective
import com.apu.neuroopdsmart.api.model.Profession
import com.apu.neuroopdsmart.api.model.SurveyProfession
import com.apu.neuroopdsmart.api.model.SurveyResult
import com.apu.neuroopdsmart.api.model.TestResult
import com.apu.neuroopdsmart.api.model.User
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import java.lang.Exception
import okhttp3.ResponseBody

class ApiService {

    val user_id = 1

    val TAG = "ApiService"

    private val retrofit = RetrofitClient.getClient()
    private val api: Api = retrofit.create(Api::class.java)

    fun getProfessions(
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (List<Profession>) -> Unit
    ) {
        val call = api.getProfessions(user_id)
            .execute()

        if (call.errorBody() != null) {
            onError(call.errorBody()!!)
        }
        onSuccess(call.body()!!)
    }

    fun getAdjectivesByCategory(
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (List<Adjective>) -> Unit
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
        onSuccess: (Profession?) -> Unit = {}
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
        surveyResult: SurveyResult,
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (SurveyResult?) -> Unit = {}
    ) {
        surveyResult.id = user_id
        val call = api.sendSurveyResult(surveyResult, user_id)
            .execute()

        if (call.errorBody() != null) {
            onError(call.errorBody()!!)
        } else {
            onSuccess(call.body())
        }
    }

    fun getSurveyResult(
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: (List<SurveyProfession>?) -> Unit = {}
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
        testResult: TestResult,
        onError: (ResponseBody) -> Unit = ::logResponse,
        onSuccess: () -> Unit = {}
    ) {
        val call = api.uploadTestResult(testResult, user_id)
            .execute()

        if (!call.isSuccessful) {
            onError(call.errorBody()!!)
        } else {
            call.body()
            onSuccess()
        }
        onSuccess()
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (ResponseBody) -> Unit
    ) {
        val call = api.login(email, password)
            .execute()

        if (call.errorBody() != null) {
            logResponse(call.errorBody()!!)
            onError(call.errorBody()!!)
        } else {
            call.body()?.let {
                onSuccess(it[0])
            }
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        isMale: Boolean,
        isCompetent: Boolean,
        onSuccess: () -> Unit,
        onError: (ResponseBody) -> Unit
    ) {
        try {
            val call = api.register(
                User.createUser(
                    username,
                    email,
                    password,
                    isMale,
                    isCompetent
                )
            )
                .execute() // fixme com.fasterxml.jackson.databind.exc.MismatchedInputException: No content to map due to end-of-input

            if (call.errorBody() != null) {
                logResponse(call.errorBody()!!)
                onError(call.errorBody()!!)
            } else {
                call.body()?.let {
                    onSuccess()
                }
            }
        } catch (e: MismatchedInputException) {
            Log.e(TAG, "register: error supressed", e)

            onSuccess()
        }
    }
}
