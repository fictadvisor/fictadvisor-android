package com.fictadvisor.android.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fictadvisor.android.R
import com.fictadvisor.android.data.dto.AuthLoginResponse
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.databinding.FragmentSecurityStudentProfileBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.utils.StorageUtil
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SecurityStudentProfileFragment : Fragment() {
    private lateinit var binding: FragmentSecurityStudentProfileBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()
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
        binding = FragmentSecurityStudentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        ).get(AuthViewModel::class.java)

        storageUtil = StorageUtil(requireContext())

        if (storageUtil.getTokens()?.accessToken != null) {
            binding.buttonChangePassword.setOnClickListener {
                val oldPassword = binding.editTextOldPassword.text.toString()
                val newPassword = binding.editTextNewPassword.text.toString()
                val token = storageUtil.getTokens()?.accessToken.toString()
                updatePassword(token, oldPassword, newPassword)
                Navigation.findNavController(view).navigate(R.id.action_securityStudentProfileFragment_to_studentProfileFragment)
            }
        }

        binding.buttonPrevious.setOnClickListener {
            view.let { it1 -> Navigation.findNavController(it1).navigateUp() }
        }

        return view
    }

    private fun updatePassword(token: String, oldPassword: String, newPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.updatePassword(token, oldPassword, newPassword)
        }

        authViewModel.authUpdatePasswordResponse.observe(viewLifecycleOwner) { updatePasswordResponse ->
            updatePasswordResponse?.let {
                handleUpdatePasswordResponse(it)
            }
        }
    }

    private fun handleUpdatePasswordResponse(response: BaseResponse<AuthLoginResponse>) {
        when (response) {
            is BaseResponse.Success -> {
                showSuccessLog("Пароль успішно змінено")
            }

            is BaseResponse.Error -> {
                showErrorLog("Помилка зміни паролю: ${response.error?.message}")
            }

            is BaseResponse.Loading -> {
                showSuccessLog("Завантаження...")
            }
        }
    }

    private fun showSuccessLog(message: String) {
        Log.d("SecurityStudentProfileFragment", message)
    }

    private fun showErrorLog(message: String) {
        Log.e("SecurityStudentProfileFragment", message)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SecurityStudentProfileFragment()
    }
}