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
import androidx.lifecycle.get
import com.fictadvisor.android.R
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.schedule.EventDTO
import com.fictadvisor.android.data.dto.schedule.GetEventResponse
import com.fictadvisor.android.data.dto.schedule.TDiscipline
import com.fictadvisor.android.databinding.FragmentScheduleBinding
import com.fictadvisor.android.repository.ScheduleRepository
import com.fictadvisor.android.utils.StorageUtil
import com.fictadvisor.android.viewmodel.ScheduleViewModel
import com.fictadvisor.android.viewmodel.ScheduleViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var scheduleViewModel: ScheduleViewModel
    private val scheduleRepository = ScheduleRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scheduleViewModel = ViewModelProvider(
            this,
            ScheduleViewModelFactory(scheduleRepository)
        ).get(ScheduleViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //обережено хард код:
            scheduleViewModel.getEvents("2d6059e8-c625-4ce8-acf8-f865823f90d8", 11)
            Log.d("yess", "1")
        }

        scheduleViewModel.getEventsResponse
            .observe(viewLifecycleOwner) { eventResponse ->
                eventResponse?.let {
                    Log.d("yess", "2")
                    handleScheduleResponse(eventResponse)
                }
            }


        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    private fun handleScheduleResponse(response:  BaseResponse<GetEventResponse>) {
        when (response) {
            is BaseResponse.Success -> {
                val events = response.data?.events ?: emptyList()
                val week = response.data?.week
                Log.d("yess", "3"+response.data?.week)


                drawUI(events)
            }

            is BaseResponse.Error -> {
            }

            is BaseResponse.Loading -> {
            }
        }
    }


    private fun drawUI(events: List<EventDTO>) {
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

        for (event in events) {
            Log.d("ScheduleFragmentHey", "$event")
            val cardView = layoutInflater.inflate(R.layout.card_view_template, null) as CardView
            cardView.id = View.generateViewId()

            TODO("переписати, не працює")
            var startTime = event.startTime
            var endTime = event.endTime
            val formatter2 = DateTimeFormatter.ofPattern("HH:mm")
            val startTimeFormatted = startTime.format(formatter2)
            val endTimeFormatted = endTime.format(formatter2)

            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                dpToPx(calculateCardHeight(startTimeFormatted, endTimeFormatted))
            )
            cardView.layoutParams = layoutParams

            val textViewSubject = cardView.findViewById<TextView>(R.id.subject)
            textViewSubject.text = event.name

            val textViewTime = cardView.findViewById<TextView>(R.id.time)
            textViewTime.text = "${event.startTime} - ${event.endTime}"

            val textViewTeacher = cardView.findViewById<TextView>(R.id.teacher)
            // textViewTeacher.text = event.

            val cardColor = when (event.disciplineType?.name) {
                TDiscipline.LECTURE -> R.color.lecture_side_on
                TDiscipline.PRACTICE -> R.color.practice_side_on
                TDiscipline.LABORATORY -> R.color.lab_side_on
                else -> R.color.other_side_on
            }
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(),
                cardColor
            ))

            val textContainer = cardView.findViewById<LinearLayout>(R.id.text_container)

            val linearColor = when (event.disciplineType?.name) {
                TDiscipline.LECTURE -> R.color.lecture_main
                TDiscipline.PRACTICE -> R.color.practice_main
                TDiscipline.LABORATORY -> R.color.lab_main
                else -> R.color.other_main
            }
            textContainer.setBackgroundColor(ContextCompat.getColor(requireContext(),
                linearColor
            ))

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
                dpToPx(calculateStartPosition(event.startTime))
            )

            constraints.connect(
                cardView.id, ConstraintSet.END,
                scheduleLayout.id, ConstraintSet.END,
                16
            )

            constraints.applyTo(scheduleLayout)
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