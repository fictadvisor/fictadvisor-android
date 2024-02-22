package com.fictadvisor.android.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.fictadvisor.android.data.dto.AuthLoginResponse
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.ResetPasswordDTO
import com.fictadvisor.android.databinding.FragmentResetPasswordBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()
    private val args: ResetPasswordFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        ).get(AuthViewModel::class.java)

        setChangePasswordButtonListener()

        return view
    }

    private fun setChangePasswordButtonListener() {
        binding.buttonChangePassword.setOnClickListener {
            if (binding.editTextNewPassword.text.toString() != binding.editTextConfirmPassword.text.toString()) {
                Toast.makeText(activity, "Паролі не співпадають", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPassword = binding.editTextNewPassword.text.toString()
            val token = args.token
            if (token != null) {
                Toast.makeText(activity, token, Toast.LENGTH_SHORT).show()
                sendResetPasswordRequest(token, newPassword)
            }
        }
    }

    private fun sendResetPasswordRequest(token: String, newPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.resetPassword(token, ResetPasswordDTO(newPassword))
        }
        authViewModel.authUpdatePasswordResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                handleRecoveryPasswordResponse(response)
            }
        }
    }

    private fun handleRecoveryPasswordResponse(response: BaseResponse<AuthLoginResponse>) {
        when (response) {
            is BaseResponse.Success -> {
                val message = response.data.toString()
                showSuccessLog("Пароль змінено: $message")
                val action = ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }

            is BaseResponse.Error -> {
                showErrorLog("Помилка при зміненні пароля: ${response.error?.message}")
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