package com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.api
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signInRequest.SignInRequest
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signInResponse.SignInResponse
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signUpRequest.SignUpUserRequest
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signUpResponse.SignUpUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MyApi {

    @POST("/users/signup")
    suspend fun signup(@Body signUpUserRequest: SignUpUserRequest) : Response<SignUpUserResponse>

    @POST("/users/signin")
    suspend fun signin(@Body signInRequest: SignInRequest) : Response<SignInResponse>
}