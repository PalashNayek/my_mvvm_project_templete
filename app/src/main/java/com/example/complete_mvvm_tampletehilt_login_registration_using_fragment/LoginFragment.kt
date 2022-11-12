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
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.databinding.FragmentLoginBinding
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.model.signInRequest.SignInRequest
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.utils.NetworkResult
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.utils.TokenManager
import com.example.complete_mvvm_tampletehilt_login_registration_using_fragment.view_model.AuthViewModel
import javax.inject.Inject

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager : TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Login button click...............................
        binding.btnLogin.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.loginUserVM(getUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        //fetching response.........................................................................
        bindObserver()
    }

    private fun getUserRequest() : SignInRequest {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return SignInRequest(email, password)
    }

    private fun validateUserInput() : Pair<Boolean, String>{
        val userRequest = getUserRequest()
        return authViewModel.validateLoginCredentials(userRequest.email, userRequest.password)
    }

    private fun bindObserver() {
        authViewModel.signInUserResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    //save Token..............
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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