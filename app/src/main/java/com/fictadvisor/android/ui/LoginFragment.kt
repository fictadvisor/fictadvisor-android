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
import com.fictadvisor.android.data.dto.OrdinaryStudentResponse
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

        setLoginButtonListener()
        setPreviousButtonListener()
        setForgotPasswordTextListener()

        return view
    }

    private fun setPreviousButtonListener() {
        binding.buttonPrevious.setOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigateUp() }
        }
    }
    private fun setLoginButtonListener() {
        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextLogin.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (inputValidator.isLoginDataValid(username)) {
                loginUser(username, password)
                val token = storageUtil.getTokens()?.accessToken
                if (token != null) {
                    getStudentInfo(token)
                }
            }
        }
    }

    private fun setForgotPasswordTextListener() {
        binding.textViewForgotPassword.setOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_loginFragment_to_forgotPasswordFragment) }
        }
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

    private fun getStudentInfo(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.getStudent(token)
        }

        authViewModel.authOrdinaryStudentResponse.observe(viewLifecycleOwner) { studentInfoResponse ->
            studentInfoResponse?.let {
                handleStudentInfoResponse(studentInfoResponse)
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

    private fun handleStudentInfoResponse(studentInfoResponse: BaseResponse<OrdinaryStudentResponse>) {
        when (studentInfoResponse) {
            is BaseResponse.Success -> {
                showSuccessLog("Інформація про студента успішно отримана")
                saveStudentInfo(studentInfoResponse)
            }

            is BaseResponse.Error -> {
                showErrorLog("Помилка отримання інформації про студента: ${studentInfoResponse.error?.message}")
            }

            is BaseResponse.Loading -> {
                // Loading, if needed
            }
        }
    }

    private fun saveStudentInfo(response: BaseResponse.Success<OrdinaryStudentResponse>) {
        val responseData = response.data!!
        storageUtil.setOrdinaryStudentInfo(responseData)
        Log.d("LoginFragment", "Student info: ${storageUtil.getOrdinaryStudentInfo()}")

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