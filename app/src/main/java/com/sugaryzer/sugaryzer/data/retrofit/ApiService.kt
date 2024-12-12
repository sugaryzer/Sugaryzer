package com.sugaryzer.sugaryzer.data.retrofit

import com.sugaryzer.sugaryzer.data.dataclass.ScannedData
import com.sugaryzer.sugaryzer.data.dataclass.SignInRequest
import com.sugaryzer.sugaryzer.data.dataclass.SignUpRequest
import com.sugaryzer.sugaryzer.data.response.AnalyticsResponse
import com.sugaryzer.sugaryzer.data.response.ConsumeResponse
import com.sugaryzer.sugaryzer.data.response.HistoryResponse
import com.sugaryzer.sugaryzer.data.response.LoginResponse
import com.sugaryzer.sugaryzer.data.response.MessageResponse
import com.sugaryzer.sugaryzer.data.response.NewsResponse
import com.sugaryzer.sugaryzer.data.response.ProfileResponse
import com.sugaryzer.sugaryzer.data.response.RecommendationResponse
import com.sugaryzer.sugaryzer.data.response.RegisterResponse
import com.sugaryzer.sugaryzer.data.response.ResultProfile
import com.sugaryzer.sugaryzer.data.response.ScanResponse
import com.sugaryzer.sugaryzer.data.response.ScannedResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @POST("register")
    suspend fun register(
        @Body SignUpRequest: SignUpRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body SignInRequest: SignInRequest
    ): LoginResponse

    @GET("articles")
    suspend fun getNews(): NewsResponse

    @GET("users/current/user-profile")
    suspend fun getProfile(): ProfileResponse

    @PATCH("users/current/user-profile")
    suspend fun updateProfile(
        @Body resultProfile: ResultProfile
    ): ProfileResponse

    @Multipart
    @POST("products/scan")
    suspend fun scanImage(
        @Part file: MultipartBody.Part,
    ): ScanResponse

    @GET("users/current/scanned-products")
    suspend fun getHistory(@Query("size") size: Int): HistoryResponse

    @POST("users/current/scanned-products")
    suspend fun uploadScan(
        @Body scannedData: ScannedData
    ): ScannedResponse

    @GET("users/current/analysis")
    suspend fun getConsume(
        @Query("date") date: String
    ) : ConsumeResponse

    @GET("users/current/analysis")
    suspend fun getAnalytics(
        @Query("daily_sugar_intake") sugar: Int
    ) : AnalyticsResponse

    @GET("products/{productBarcode}/recommendations")
    suspend fun getRecommendations(
        @Path("productBarcode") productBarcode: String
    ): RecommendationResponse
}