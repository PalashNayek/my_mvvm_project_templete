package com.example.complete_mvvm_tampletehilt_login_registration_using_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.databinding.FragmentRegistrationBinding
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signUpRequest.SignUpUserRequest
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.utils.NetworkResult
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.utils.TokenManager
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.view_model.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager : TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        if (tokenManager.getToken() != null){
            findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Already have an account click..............................
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        //SignUp button click.................................
        binding.btnSignUp.setOnClickListener {

            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.registerUserVM(getUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }
        }

        //fetching signup response.........................................................................
        bindObserver()
    }

    private fun getUserRequest(): SignUpUserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val userName = binding.txtUsername.text.toString()

        return SignUpUserRequest(emailAddress, password, userName)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateRegisterCredentials(
            userRequest.username,
            userRequest.email,
            userRequest.password
        )
    }

    private fun bindObserver() {
        authViewModel.signUpUserResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    //save Token..............
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                else -> {
                    Toast.makeText(context, "Something app error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}