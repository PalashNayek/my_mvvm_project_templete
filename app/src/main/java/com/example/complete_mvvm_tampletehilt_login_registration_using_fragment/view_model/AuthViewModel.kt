package com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.view_model

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signInRequest.SignInRequest
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signInResponse.SignInResponse
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signUpRequest.SignUpUserRequest
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signUpResponse.SignUpUserResponse
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.repository.UserRepository
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (private val userRepository: UserRepository) : ViewModel() {

    val signUpUserResponseLiveData : LiveData<NetworkResult<SignUpUserResponse>>
        get() = userRepository.signUpResponseLiveData

    val signInUserResponseLiveData : LiveData<NetworkResult<SignInResponse>>
        get() = userRepository.signInResponseLiveData

    fun registerUserVM(signUpUserRequest: SignUpUserRequest){
        viewModelScope.launch {
            userRepository.registerUser(signUpUserRequest)
        }
    }

    fun loginUserVM(signInRequest: SignInRequest){
        viewModelScope.launch {
            userRepository.loginUser(signInRequest)
        }
    }

    fun validateRegisterCredentials(userName : String, emailAddress: String, password: String) : Pair<Boolean, String>{
        var result = Pair(true, "")

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please fill-up all field")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){

            result = Pair(false, "Please provide valid email")
        }else if (password.length<=5){
            result = Pair(false, "Password length should be greater then 5")
        }

        return result

    }
    fun validateLoginCredentials(emailAddress: String, password: String ) : Pair<Boolean, String>{
        var result = Pair(true, "")

        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please fill-up all field")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){

            result = Pair(false, "Please provide valid email")
        }else if (password.length<=5){
            result = Pair(false, "Password length should be greater then 5")
        }
        return result
    }
}