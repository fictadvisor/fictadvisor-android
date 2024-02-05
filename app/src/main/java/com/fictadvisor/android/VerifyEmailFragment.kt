package com.fictadvisor.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.fictadvisor.android.data.dto.AuthLoginResponse
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.RegistrationDTO
import com.fictadvisor.android.data.dto.VerificationEmailDTO
import com.fictadvisor.android.databinding.FragmentRegistrationBinding
import com.fictadvisor.android.databinding.FragmentVerifyEmailBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.utils.StorageUtil
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VerifyEmailFragment : Fragment() {
    private lateinit var binding: FragmentVerifyEmailBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()
    private val args: RegistrationFragmentArgs by navArgs()
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
        binding = FragmentVerifyEmailBinding.inflate(inflater, container, false)
        val view = binding.root

        storageUtil = StorageUtil(requireContext())

        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        ).get(AuthViewModel::class.java)

        val token = args.token
        if (token != null) {
            Toast.makeText(activity, token, Toast.LENGTH_SHORT).show()
            binding.messageTV.text = "Надсилаємо запит на підтвердження електронної пошти..."
            sendEmailVerificationRequest(token)
        }

        return view
    }

    private fun sendEmailVerificationRequest(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.verifyEmailToken(token)
        }

        authViewModel.authVerifyEmailTokenResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                handleVerificationEmailResponse(response)
            }
        }
    }

    private fun handleVerificationEmailResponse(response: BaseResponse<AuthLoginResponse>) {
        when (response) {
            is BaseResponse.Success -> {
                binding.messageTV.text = "Email успішно підтверджено!"
                saveRefreshAndAccessTokens(response)
                // TODO: go to the authorized part of the program, as we now have the tokens for auth
            }

            is BaseResponse.Error -> {
                binding.messageTV.text = "Помилка при підтвердженні email: ${response.error?.message}"
            }

            is BaseResponse.Loading -> {}
        }
    }

    private fun saveRefreshAndAccessTokens(response: BaseResponse.Success<AuthLoginResponse>) {
        val responseData = response.data!!
        val accessToken = responseData.accessToken
        val refreshToken = responseData.refreshToken
        storageUtil.setTokens(accessToken, refreshToken)
    }
}