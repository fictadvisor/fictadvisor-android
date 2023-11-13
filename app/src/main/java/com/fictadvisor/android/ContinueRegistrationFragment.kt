package com.fictadvisor.android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fictadvisor.android.data.dto.AuthLoginResponse
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.StudentDTO
import com.fictadvisor.android.data.dto.TelegramDTO
import com.fictadvisor.android.data.dto.UserDTO
import com.fictadvisor.android.databinding.FragmentContinueRegistrationBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContinueRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentContinueRegistrationBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContinueRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        ).get(AuthViewModel::class.java)

        setupUI()
        return view
    }

    private fun setupUI() {
        binding.buttonBack.setOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigateUp() }
        }

        val arguments = arguments
        if (arguments != null) {
            val username = arguments.getString("username")
            val name = arguments.getString("name")
            val lastname = arguments.getString("lastname")
            val middleName = arguments.getString("middleName")
            val group = arguments.getString("group")

            if (username != null && name != null && lastname != null && middleName != null && group != null ) {

                        binding.buttonRegister.setOnClickListener {
                            val email = binding.editTextTextEmail.text.toString()
                            val password = binding.editTextPassword.text.toString()
                            val passwordConfirm = binding.editTextTextConfirmPass.text.toString()
                            if (email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
                                validateAndRegisterUser(
                                    username,
                                    email,
                                    name,
                                    lastname,
                                    middleName,
                                    group,
                                    password,
                                    passwordConfirm
                                )
                            }
                }
            }
        }
    }

    private fun validateAndRegisterUser(
        username: String,
        email: String,
        name: String,
        lastname: String,
        middleName: String,
        group: String,
        password: String,
        passwordConfirm: String
    ) {

        if (password != passwordConfirm) {
            showErrorMessage("Паролі не співпадають")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.verifyIsRegistered(username, email)
        }

        authViewModel.authIsRegisterResponse.observe(viewLifecycleOwner) { response ->
            response?.let {

                handleIsRegisteredResponse(it, username, email, name, lastname, middleName, group, password)
            }
        }
    }

    private fun handleIsRegisteredResponse(
        response: BaseResponse<Boolean>,
        username: String,
        email: String,
        name: String,
        lastname: String,
        middleName: String,
        group: String,
        password: String
    ) {
        when (response) {
            is BaseResponse.Success -> {
                if (response.data == true) {
                    if (binding.checkBoxCaptain.isChecked) {
                        CoroutineScope(Dispatchers.IO).launch {
                            authViewModel.checkCaptain(group)
                        }
                        authViewModel.authCheckCaptainResponse.observe(viewLifecycleOwner) { captainResponse ->
                            captainResponse?.let {
                                handleCaptainCheckResponse(captainResponse)
                            }
                        }
                    } else {
                        registerUser(username, email, name, lastname, middleName, group, password)
                    }
                } else {
                    registerUser(username, email, name, lastname, middleName, group, password)
                }
            }

            is BaseResponse.Error -> {
                showErrorLog("IsRegistered error: ${response.error}")
            }

            is BaseResponse.Loading -> {
                // Loading, if needed
            }
        }
    }

    private fun handleCaptainCheckResponse(captainResponse: BaseResponse<Boolean>) {
        when (captainResponse) {
            is BaseResponse.Success -> {
                showErrorMessage("Староста для групи уже призначений")
            }

            is BaseResponse.Error -> {
                showErrorLog("Check captain error: ${captainResponse.error}")
            }

            is BaseResponse.Loading -> {
                // Loading, if needed
            }
        }
    }

    private fun registerUser(
        username: String,
        email: String,
        name: String,
        lastname: String,
        middleName: String,
        group: String,
        password: String
    ) {
        val studentInfo = StudentDTO(
            groupId = group,
            firstName = name,
            middleName = middleName,
            lastName = lastname,
            isCaptain = binding.checkBoxCaptain.isChecked
        )
        val userInfo = UserDTO(username = username, email = email, password = password)
        val telegramInfo = TelegramDTO(
            authDate = 0,
            firstName = name,
            lastName = lastname,
            photoUrl = "",
            hash = "",
            id = 0,
            username = ""
        )

        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.register(studentInfo, userInfo, telegramInfo)
        }

        authViewModel.authRegisterResponse.observe(viewLifecycleOwner) { registerResponse ->
            registerResponse?.let {
                handleRegistrationResponse(registerResponse)
            }
        }
    }

    private fun handleRegistrationResponse(registerResponse: BaseResponse<AuthLoginResponse>) {
        when (registerResponse) {
            is BaseResponse.Success -> {
                showSuccessMessage("Реєстрація успішна")
            }

            is BaseResponse.Error -> {
                showErrorMessage("Помилка реєстрації: ${registerResponse.error?.message}")
            }

            is BaseResponse.Loading -> {
                // Loading, if needed
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccessMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorLog(message: String) {
        Log.e("ContinueRegistrationFragment", message)
    }

}
