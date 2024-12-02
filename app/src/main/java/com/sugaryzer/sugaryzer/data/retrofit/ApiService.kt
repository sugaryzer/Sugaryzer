package com.sugaryzer.sugaryzer.data.retrofit

import com.sugaryzer.sugaryzer.data.SignInRequest
import com.sugaryzer.sugaryzer.data.response.LoginResponse
import com.sugaryzer.sugaryzer.data.response.MessageResponse
import com.sugaryzer.sugaryzer.data.response.NewsResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("image") image: String,
        @Field("height") height: Int,
        @Field("weight") weight: Int,
        @Field("age") age: Int,
        @Field("password") password: String
    ): MessageResponse

    @POST("login")
    suspend fun login(
        @Body signInRequest: SignInRequest
    ): LoginResponse

    @GET("articles")
    suspend fun news(): NewsResponse
}