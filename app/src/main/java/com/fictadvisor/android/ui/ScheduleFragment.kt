package com.fictadvisor.android.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fictadvisor.android.R
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.OrdinaryStudentResponse
import com.fictadvisor.android.data.dto.schedule.DetailedEventResponse
import com.fictadvisor.android.data.dto.schedule.EventDTO
import com.fictadvisor.android.data.dto.schedule.GetEventResponse
import com.fictadvisor.android.data.dto.schedule.PatchEventDTO
import com.fictadvisor.android.data.dto.schedule.PostEventDTO
import com.fictadvisor.android.data.dto.schedule.TDiscipline
import com.fictadvisor.android.data.dto.schedule.TEventPeriod
import com.fictadvisor.android.data.dto.schedule.Teacher
import com.fictadvisor.android.databinding.FragmentScheduleBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.repository.ScheduleRepository
import com.fictadvisor.android.utils.StorageUtil
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import com.fictadvisor.android.viewmodel.ScheduleViewModel
import com.fictadvisor.android.viewmodel.ScheduleViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.TimeZone

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
                .into(binding.userAvatar)
        }
        binding.userAvatar.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_scheduleFragment_to_studentProfileFragment)
        }

         scheduleViewModel = ViewModelProvider(
            this,
            ScheduleViewModelFactory(scheduleRepository)
        ).get(ScheduleViewModel::class.java)

        // testScheduleApiMethods()

        val token = "Bearer ${StorageUtil(requireContext()).getTokens()!!.accessToken}"
        CoroutineScope(Dispatchers.IO).launch {
            scheduleViewModel.getEventsAuthorized(
                token,
                storageUtil.getOrdinaryStudentInfo()!!.group.id,
                11,
                false
            )
        }

        scheduleViewModel.getEventsAuthorizedResponse
            .observe(viewLifecycleOwner) { eventResponse ->
                eventResponse?.let {
                    handleScheduleResponse(eventResponse)
                }
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
            val eventId =
                "99eb3fdb-3a95-4ec7-b911-eae752a1de48" // TODO: replace every time you delete a test event
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

    // works but wrong

    private var listOfEvents = mutableListOf<EventDTO>()

    private fun handleScheduleResponse(response:  BaseResponse<GetEventResponse>) {
        when (response) {
            is BaseResponse.Success -> {
                val events = response.data?.events ?: emptyList()
                val week = response.data?.week

                for (event in events) {
                    var teachers: List<Teacher>
                    var disciplineType: TDiscipline
                    Log.d("yess", event.toString())

                    val token = "Bearer ${StorageUtil(requireContext()).getTokens()!!.accessToken}"
                    CoroutineScope(Dispatchers.IO).launch {
                        scheduleViewModel.getEventInfo(token, event.id, week!!.toInt())
                        Log.d("yess", "3")
                    }

                    scheduleViewModel.getEventInfoResponse
                        .observe(viewLifecycleOwner) { eventInfoResponse ->
                            eventInfoResponse?.let {
                                Log.d("yess", "4")
                                handle(eventInfoResponse, event, week!!)
                                Log.d("yess", event.toString())
                            }
                        }
                }
            }

            is BaseResponse.Error -> {
            }

            is BaseResponse.Loading -> {
            }
        }
    }

    class EventInfo(
        val name: String,
        val teacher: List<Teacher>,
        val startTime: String,
        val endTime: String,
        val type:  TDiscipline,
    )

    private fun handle(eventInfoResponse:  BaseResponse<DetailedEventResponse>, event: EventDTO, week: String) {
        when (eventInfoResponse) {
            is BaseResponse.Success -> {
                Log.d("yess", "5")
                Log.d("yess", eventInfoResponse.toString())

                listOfEvents.add(event)
                val teachers = eventInfoResponse.data?.teachers ?: emptyList()
                val disciplineType = eventInfoResponse.data!!.eventType
                val events = mutableListOf<EventInfo>()
                if (getDate(event.startTime) == "2024-04-15") {
                    Log.d("yess", event.toString())
                    events.add(
                        EventInfo(
                            event.name,
                            teachers,
                            event.startTime,
                            event.endTime,
                            disciplineType
                        )
                    )
                }
                //Log.d("yess", events.toString())

                drawUI(events, week)
            }

            is BaseResponse.Error -> {
                Log.d("yess", "error")
                Log.d("yess", eventInfoResponse.toString())
            }

            is BaseResponse.Loading -> {
            }
        }
    }

    private fun getTime(string: String): String {
        val defaultTimezone = TimeZone.getDefault().id
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.UK)
        val date = dateFormat.parse(string.replace("Z$", "+0000"))

        val offsetDateTime = OffsetDateTime.parse(dateFormat.format(date!!), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"))

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val time = offsetDateTime.format(timeFormatter)

        Log.d("TimeSchedule", "string: $string")
        Log.d("TimeSchedule", "defaultTimezone: $defaultTimezone")
        Log.d("TimeSchedule", "date: $time")

        return time
    }

    private fun getDate(string: String): String {
        val offsetDateTime = OffsetDateTime.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"))

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val newDate = offsetDateTime.format(dateFormatter)

        Log.d("TimeSchedule", "string: $string")
        Log.d("TimeSchedule", "date: $newDate")

        return newDate
    }

    private fun drawUI(events: List<EventInfo>, week: String?) {
        val timeColumn = binding.timeColumn
        for (i in 8 until 23) {
            val textView = TextView(requireContext())
            textView.text = "${i}:00"
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                dpToPx(20)
            )

            val marginLayoutParams = textView.layoutParams as ViewGroup.MarginLayoutParams
            val marginLeft = dpToPx(16)
            val marginTop = dpToPx(30)
            val marginRight = dpToPx(16)
            val marginBottom = dpToPx(30)

            marginLayoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)

            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grayText))

            timeColumn.addView(textView)
        }

        val scheduleLayout = binding.scheduleLayout
        val chosenDate = "2024-04-15"

        for (event in events) {
            if (getDate(event.startTime) == chosenDate) {

                Log.d("ScheduleFragmentHey", "$event")
                val cardView = layoutInflater.inflate(R.layout.card_view_template, null) as CardView
                cardView.id = View.generateViewId()

                val startTimeFormatted = getTime(event.startTime)
                val endTimeFormatted = getTime(event.endTime)

                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                    dpToPx(calculateCardHeight(startTimeFormatted, endTimeFormatted))
                )
                cardView.layoutParams = layoutParams

                val textViewSubject = cardView.findViewById<TextView>(R.id.subject)
                textViewSubject.text = event.name

                val textViewTime = cardView.findViewById<TextView>(R.id.time)
                textViewTime.text = "$startTimeFormatted - $endTimeFormatted"

                val teacher = event.teacher[0]
                val lastName = teacher.lastName
                val name = teacher.firstName[0]
                val fatherName = teacher.middleName[0]

                val textViewTeacher = cardView.findViewById<TextView>(R.id.teacher)
                textViewTeacher.text = lastName + " " + name + ". " + fatherName + "."


                val cardColor = when (event.type?.name) {
                    TDiscipline.LECTURE.toString() -> R.color.lecture_side_on
                    TDiscipline.PRACTICE.toString() -> R.color.practice_side_on
                    TDiscipline.LABORATORY.toString() -> R.color.lab_side_on
                    else -> R.color.other_side_on
                }
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        cardColor
                    )
                )

                val textContainer = cardView.findViewById<LinearLayout>(R.id.text_container)

                val linearColor = when (event.type?.name) {
                    TDiscipline.LECTURE.toString() -> R.color.lecture_main
                    TDiscipline.PRACTICE.toString() -> R.color.practice_main
                    TDiscipline.LABORATORY.toString() -> R.color.lab_main
                    else -> R.color.other_main
                }
                textContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        linearColor
                    )
                )

                scheduleLayout.addView(cardView)

                val constraints = ConstraintSet()
                constraints.clone(scheduleLayout)

                constraints.connect(
                    cardView.id, ConstraintSet.START,
                    timeColumn.id, ConstraintSet.END,
                    10
                )

                constraints.connect(
                    cardView.id, ConstraintSet.TOP,
                    scheduleLayout.id, ConstraintSet.TOP,
                    dpToPx(calculateStartPosition(startTimeFormatted))
                )

                constraints.connect(
                    cardView.id, ConstraintSet.END,
                    scheduleLayout.id, ConstraintSet.END,
                    16
                )

                constraints.applyTo(scheduleLayout)
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale).toInt()
    }

    private fun calculateCardHeight(startTime: String, endTime: String): Int {
        val startHour = startTime.substringBefore(":").toInt()
        val endHour = endTime.substringBefore(":").toInt()
        val startMinute = startTime.substringAfter(":").toInt()
        val endMinute = endTime.substringAfter(":").toInt()

        val totalStartMinutes = startHour * 60 + startMinute
        val totalEndMinutes = endHour * 60 + endMinute

        return ((totalEndMinutes - totalStartMinutes) * 80) / 60
    }

    private fun calculateStartPosition(startTime: String): Int {
        val fromLine = 40
        val startHour = startTime.substringBefore(":").toInt()
        val startMinute = startTime.substringAfter(":").toInt()

        val totalStartMinutes = (startHour * 60 - 8 * 60 + startMinute) * 80 / 60
        return totalStartMinutes + fromLine
    }

    companion object {
        @JvmStatic
        fun newInstance(): ScheduleFragment {
            return ScheduleFragment()
        }
    }
}