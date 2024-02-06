package com.fictadvisor.android.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.databinding.FragmentForgotPasswordBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.validator.LoginInputValidator
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()
    private lateinit var inputValidator: LoginInputValidator
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        inputValidator = LoginInputValidator(requireContext())

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        ).get(AuthViewModel::class.java)

        setSendButtonListener()
        setPreviousButtonListener()

        return view
    }

    private fun setPreviousButtonListener() {
        binding.buttonPrevious.setOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigateUp() }
        }
    }
    private fun setSendButtonListener() {
        binding.buttonSend.setOnClickListener {
            val email = binding.editTextSendEmail.text.toString()
            if(inputValidator.isEmailValid(email).isValid){
                sendRecoveryPasswordRequest(email)
            }
        }
    }

    private fun sendRecoveryPasswordRequest(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.forgotPassword(email)
        }
        authViewModel.authForgotPasswordResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                handleRecoveryPasswordResponse(response)
            }
        }
    }

    private fun handleRecoveryPasswordResponse(response: BaseResponse<ResponseBody>) {
        when (response) {
            is BaseResponse.Success -> {
                val message = response.data.toString()
                showSuccessLog("Лист надіслано: $message")
                val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
            is BaseResponse.Error -> {
                showErrorLog("Помилка реєстрації: ${response.error?.message}")
            }

            is BaseResponse.Loading -> {
                showSuccessLog("Завантаження...")
            }
        }
    }

    private fun showSuccessLog(message: String) {
        Log.d("ForgotPasswordFragment", message)
    }

    private fun showErrorLog(message: String) {
        Log.e("ForgotPasswordFragment", message)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ForgotPasswordFragment {
            return ForgotPasswordFragment()
        }
    }
}