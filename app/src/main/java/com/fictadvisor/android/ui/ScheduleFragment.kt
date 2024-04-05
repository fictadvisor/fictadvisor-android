package com.fictadvisor.android.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fictadvisor.android.R
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.OrdinaryStudentResponse
import com.fictadvisor.android.databinding.FragmentScheduleBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.utils.StorageUtil
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
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
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val view = binding.root
        storageUtil = StorageUtil(requireContext())
        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        ).get(AuthViewModel::class.java)
        val userToken = storageUtil.getTokens()?.accessToken
        if (userToken != null) {
            getStudentInfo(userToken)
            val userData = storageUtil.getOrdinaryStudentInfo()
            Glide.with(this).load(userData?.avatar).apply(RequestOptions.circleCropTransform())
                .into(binding.imageButton)
        }
        binding.imageButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_scheduleFragment_to_studentProfileFragment)
        }
        return view
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

    private fun showSuccessLog(message: String) {
        Log.d("LoginFragment", message)
    }

    private fun showErrorLog(message: String) {
        Log.e("LoginFragment", message)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ScheduleFragment {
            return ScheduleFragment()
        }
    }
}