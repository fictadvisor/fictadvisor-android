package com.fictadvisor.android.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fictadvisor.android.R
import com.fictadvisor.android.data.dto.AuthLoginResponse
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.databinding.FragmentLoginBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.utils.StorageUtil
import com.fictadvisor.android.validator.LoginInputValidator
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()
    private lateinit var inputValidator: LoginInputValidator
    private lateinit var storageUtil: StorageUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        inputValidator = LoginInputValidator(requireContext())
        storageUtil = StorageUtil(requireContext())

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        ).get(AuthViewModel::class.java)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextLogin.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (inputValidator.isLoginDataValid(username)) {
                loginUser(username, password)
            }
        }

        binding.buttonPrevious.setOnClickListener {
            view.let { it1 -> Navigation.findNavController(it1).navigateUp() }
        }

        binding.textViewForgotPassword.setOnClickListener {
            view.let { it1 -> Navigation.findNavController(it1)
                .navigate(R.id.action_loginFragment_to_forgotPasswordFragment) }
        }


        return view
    }

    private fun loginUser(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.login(username, password)
        }

        authViewModel.authLoginResponse.observe(viewLifecycleOwner) { registerResponse ->
            registerResponse?.let {
                handleLoginResponse(registerResponse)
            }
        }

    }

    private fun handleLoginResponse(registerResponse: BaseResponse<AuthLoginResponse>) {
        when (registerResponse) {
            is BaseResponse.Success -> {
                showSuccessLog("Авторизація успішна")
                saveRefreshAndAccessTokens(registerResponse)
                val action = LoginFragmentDirections.actionLoginFragmentToScheduleFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }

            is BaseResponse.Error -> {
                showErrorLog("Помилка входу: ${registerResponse.error?.message}")
            }

            is BaseResponse.Loading -> {
                // Loading, if needed
            }
        }
    }

    private fun saveRefreshAndAccessTokens(response: BaseResponse.Success<AuthLoginResponse>) {
        val responseData = response.data!!
        val accessToken = responseData.accessToken
        val refreshToken = responseData.refreshToken
        storageUtil.setTokens(accessToken, refreshToken)
        Log.d("LoginFragment", "Access token: ${storageUtil.getTokens()?.accessToken}")
    }

    private fun showSuccessLog(message: String) {
        Log.d("LoginFragment", message)
    }

    private fun showErrorLog(message: String) {
        Log.e("LoginFragment", message)
    }

    companion object {
        @JvmStatic
        fun newInstance(): LoginFragment {
            return LoginFragment()
            }
    }
}