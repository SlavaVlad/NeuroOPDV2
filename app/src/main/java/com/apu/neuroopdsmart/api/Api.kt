package com.apu.neuroopdsmart.api

import com.apu.neuroopdsmart.api.model.Adjective
import com.apu.neuroopdsmart.api.model.Profession
import com.apu.neuroopdsmart.api.model.SurveyProfession
import com.apu.neuroopdsmart.api.model.SurveyResult
import com.apu.neuroopdsmart.api.model.TestResult
import com.apu.neuroopdsmart.api.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @GET("d4e55nfh5r1f62ias57t")
    fun getProfessions(@Query("id") userId: String?): Call<List<Profession>?>

    @GET("d4e94grr3ofgngk0fov4")
    fun getSurveyResults(): Call<List<SurveyProfession>?>

    @GET("d4ered3oh5uma8ti8puf")
    fun getAdjectivesByCategory(): Call<List<Adjective>?>

    @POST("d4e77hv6o71birq0vgv5")
    fun addProfession(@Body profession: Profession): Call<Profession>

    @POST("d4e4aght279t2va56gvc")
    fun sendSurveyResult(@Body surveyResult: SurveyResult, @Query("id") userId: Int): Call<SurveyResult>

    @POST("d4eer2da3m8dd5a4v2pb")
    fun uploadTestResult(@Body surveyResult: TestResult, @Header("id") userId: Int): Call<TestResult>

    @GET("d4e4osokceavevenpe15")
    fun login(@Query("email") email: String, @Query("pass") password: String): Call<Boolean>

    @POST("d4edchn1p79rhemhmog0")
    fun register(@Body user: User): Call<User>
}
