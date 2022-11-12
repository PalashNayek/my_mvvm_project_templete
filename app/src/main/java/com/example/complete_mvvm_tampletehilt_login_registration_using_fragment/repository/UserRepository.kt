package com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.api.MyApi
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signInRequest.SignInRequest
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signInResponse.SignInResponse
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signUpRequest.SignUpUserRequest
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signUpResponse.SignUpUserResponse
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val myApi: MyApi) {

    private val _signUpResponse = MutableLiveData<NetworkResult<SignUpUserResponse>>()
    val signUpResponseLiveData : LiveData<NetworkResult<SignUpUserResponse>>
        get() = _signUpResponse

    private val _signInResponse = MutableLiveData<NetworkResult<SignInResponse>>()
    val signInResponseLiveData : LiveData<NetworkResult<SignInResponse>>
        get() = _signInResponse

    suspend fun registerUser(signUpUserRequest: SignUpUserRequest){
        _signUpResponse.postValue(NetworkResult.Loading())
        val response = myApi.signup(signUpUserRequest)
        handleSignUpResponse(response)
    }

    suspend fun loginUser(signInRequest: SignInRequest){
        _signInResponse.postValue(NetworkResult.Loading())
        val response = myApi.signin(signInRequest)
        handleSignInResponse(response)
    }

    private fun handleSignUpResponse(response: Response<SignUpUserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _signUpResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _signUpResponse.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _signUpResponse.postValue(NetworkResult.Error("Something went to wrong"))
        }
    }

    private fun handleSignInResponse(response: Response<SignInResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _signInResponse.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _signInResponse.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _signInResponse.postValue(NetworkResult.Error("Something went to wrong"))
        }
    }
}