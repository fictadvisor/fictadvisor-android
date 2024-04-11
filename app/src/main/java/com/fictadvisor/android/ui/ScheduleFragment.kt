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
import com.fictadvisor.android.data.dto.ResetPasswordDTO
import com.fictadvisor.android.data.dto.schedule.DetailedEventResponse
import com.fictadvisor.android.data.dto.schedule.PatchEventDTO
import com.fictadvisor.android.data.dto.schedule.PostEventDTO
import com.fictadvisor.android.data.dto.schedule.TDiscipline
import com.fictadvisor.android.data.dto.schedule.TEventPeriod
import com.fictadvisor.android.databinding.FragmentResetPasswordBinding
import com.fictadvisor.android.repository.ScheduleRepository
import com.fictadvisor.android.viewmodel.ScheduleViewModel
import com.fictadvisor.android.viewmodel.ScheduleViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var authViewModel: AuthViewModel
    private val authRepository = AuthRepository()
    private lateinit var storageUtil: StorageUtil
    private lateinit var scheduleViewModel: ScheduleViewModel
    private val scheduleRepository = ScheduleRepository()

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
        
         scheduleViewModel = ViewModelProvider(
            this,
            ScheduleViewModelFactory(scheduleRepository)
        ).get(ScheduleViewModel::class.java)

        testScheduleApiMethods()

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


    private fun testScheduleApiMethods() {
//        testGetEvents()
//        testGetEventsAuthorized()
//        testAddEvent()
        testGetEventInfo()
//        testEditEvent()
//        testDeleteEventById()
    }

    private fun testGetEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            scheduleViewModel.getEvents("b49dd1d3-5111-4c74-a10c-768f958a19da", 2)
        }
        scheduleViewModel.getEventsResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("getEvents", response.toString())
                if (response is BaseResponse.Success) {
                    Log.d("getEvents", (response.data!!).toString())
                }
            }
        }
    }

    private fun testGetEventsAuthorized() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer ${StorageUtil(requireContext()).getTokens()!!.accessToken}"
            scheduleViewModel.getEventsAuthorized(token, "b49dd1d3-5111-4c74-a10c-768f958a19da", 1, false)
        }
        scheduleViewModel.getEventsAuthorizedResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("getEventsAuthorized", response.toString())
                if (response is BaseResponse.Success) {
                    Log.d("getEventsAuthorized", (response.data!!).toString())
                }
            }
        }
    }

    private fun testAddEvent() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer ${StorageUtil(requireContext()).getTokens()!!.accessToken}"
            val groupId = "b49dd1d3-5111-4c74-a10c-768f958a19da"
            val testPostEventDTO = PostEventDTO(
                groupId = groupId,
                name = "Some event",
                teachers = listOf("229ab047-2e97-4267-91bc-d5f211891c90"), // Роковий
                disciplineId = "080eab0a-24af-406c-879d-91afb1751bfb", // ММІТ
                url = "https://example.com",
                eventInfo = "This is a test event",
                eventType = TDiscipline.LECTURE,
                disciplineInfo = "Some discipline info",
                period = TEventPeriod.EVERY_WEEK,
                startTime = "2024-04-08T14:15:00.823Z",
                endTime = "2024-04-08T15:50:00.823Z"
            )
            scheduleViewModel.addEvent(token, testPostEventDTO, groupId)
        }
        scheduleViewModel.addEventResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("addEvent", response.toString())
                if (response is BaseResponse.Success) {
                    Log.d("addEvent", (response.data!!).toString())
                }
            }
        }
    }

    private fun testGetEventInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer ${StorageUtil(requireContext()).getTokens()!!.accessToken}"
            val eventId = "99eb3fdb-3a95-4ec7-b911-eae752a1de48" // TODO: replace every time you delete a test event
            scheduleViewModel.getEventInfo(token, eventId, 10) // use different ids, week number is counted from the start of the term
        }
        scheduleViewModel.getEventInfoResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("getEventInfo", response.toString())
                if (response is BaseResponse.Success) {
                    Log.d("getEventInfo", (response.data!!).toString())
                }
            }
        }
    }

    private fun testEditEvent() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer ${StorageUtil(requireContext()).getTokens()!!.accessToken}"
            val groupId = "b49dd1d3-5111-4c74-a10c-768f958a19da"
            val eventId = "99eb3fdb-3a95-4ec7-b911-eae752a1de48" // TODO: replace every time you delete a test event
            val testPatchEventDTO = PatchEventDTO(
                week = 10,
                name = "Some event",
                changeStartDate = false,
                changeEndDate = false,
                eventType = TDiscipline.LECTURE,
                url = "https://example.com",
                eventInfo = "This is a test event",
                disciplineInfo = "Some discipline info",
                period = TEventPeriod.EVERY_WEEK,
                teachers = listOf("229ab047-2e97-4267-91bc-d5f211891c90"), // Роковий
                disciplineId = "080eab0a-24af-406c-879d-91afb1751bfb", // ММІТ
                startTime = "2024-04-08T14:15:00.823Z",
                endTime = "2024-04-08T15:50:00.823Z"
            )
            scheduleViewModel.editEvent(token, testPatchEventDTO, groupId, eventId)
        }
        scheduleViewModel.editEventResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("editEvent", response.toString())
                if (response is BaseResponse.Success) {
                    Log.d("editEvent", (response.data!!).toString())
                }
            }
        }
    }

    private fun testDeleteEventById() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = "Bearer ${StorageUtil(requireContext()).getTokens()!!.accessToken}"
            val groupId = "b49dd1d3-5111-4c74-a10c-768f958a19da"
            val eventId = "99eb3fdb-3a95-4ec7-b911-eae752a1de48" // TODO: replace every time you delete a test event
            scheduleViewModel.deleteEventById(token, groupId, eventId)
        }
        scheduleViewModel.deleteEventByIdResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("deleteEventById", response.toString())
                if (response is BaseResponse.Success) {
                    Log.d("deleteEventById", (response.data!!).toString())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): ScheduleFragment {
            return ScheduleFragment()
        }
    }
}