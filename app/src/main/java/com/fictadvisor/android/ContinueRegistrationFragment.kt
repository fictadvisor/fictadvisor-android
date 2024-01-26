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
import com.fictadvisor.android.validator.InputValidator
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

        setBackButtonListener()
        setRegisterButtonListener()
        return view
    }

    private fun setBackButtonListener() {
        binding.buttonBack.setOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigateUp() }
        }
    }

    private fun setRegisterButtonListener() {
        binding.buttonRegister.setOnClickListener {
            val studentData = getStudentData()
            val userData = getUserData()
            val telegramData = getTelegramData()

            if (userData.isEmpty() || studentData.isEmpty()) {
                showErrorMessage("Будь ласка, введіть правильні дані")
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                authViewModel.verifyIsRegistered(userData.username, userData.email)
            }

            authViewModel.authIsRegisterResponse.observe(viewLifecycleOwner) { response ->
                response?.let {

                    handleIsRegisteredResponse(it, studentData, userData, telegramData)
                }
            }
        }
    }

    private fun getTelegramData (): TelegramDTO {
        //TODO:get telegram data from server
        return TelegramDTO(0, "", "", "", "", 0)
    }

    private fun getStudentData(): StudentDTO {
        val arguments = arguments
        if (arguments != null) {
            val group = arguments.getString("group")
            val name = arguments.getString("name")
            val middleName = arguments.getString("middleName")
            val lastname = arguments.getString("lastname")
            val isCaptain = binding.checkBoxCaptain.isChecked

            if (name != null && lastname != null && middleName != null && group != null) {
                return StudentDTO(
                    groupId = group,
                    firstName = name,
                    middleName = middleName,
                    lastName = lastname,
                    isCaptain = isCaptain
                )
            }
        }
        return StudentDTO("", "", "", "", false)
    }

    private fun getUserData(): UserDTO {
        val email = binding.editTextTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val passwordConfirm = binding.editTextTextConfirmPass.text.toString()
        if(!isInputValid(email, password, passwordConfirm)){
            return UserDTO("", "", "")
        }
        val arguments = arguments
        if (arguments != null) {
            val username = arguments.getString("username")
            if (username != null) {
                return UserDTO(username = username, email = email, password = password)
            }
        }
        return UserDTO("", "", "")
    }

    private fun isInputValid(email: String, password: String, passwordConfirm: String): Boolean {
        val emailValidationResult = InputValidator.isEmailValid(email)
        val passwordValidationResult = InputValidator.isPasswordValid(password)

        if (!emailValidationResult.isValid) {
            showErrorMessage(emailValidationResult.errorMessage)
            return false
        }
        if (!passwordValidationResult.isValid) {
            showErrorMessage(passwordValidationResult.errorMessage)
            return false
        }
        if(password != passwordConfirm) {
            showErrorMessage("Паролі не співпадають")
            return false
        }
        return true
    }

    private fun handleIsRegisteredResponse(
        response: BaseResponse<Boolean>, studentData: StudentDTO, userData: UserDTO, telegramData: TelegramDTO
    ) {
        when (response) {
            is BaseResponse.Success -> {
                if (response.data == true) {
                    if (studentData.isCaptain) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val group = studentData.groupId
                            authViewModel.checkCaptain(group)
                        }
                        authViewModel.authCheckCaptainResponse.observe(viewLifecycleOwner) { captainResponse ->
                            captainResponse?.let {
                                handleCaptainCheckResponse(captainResponse)
                            }
                        }
                    } else {
                        registerUser(studentData, userData, telegramData)
                    }
                } else {
                    registerUser(studentData, userData, telegramData)
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
        studentData: StudentDTO, userData: UserDTO, telegramData: TelegramDTO
    ) {

        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.register(studentData, userData, telegramData)
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
