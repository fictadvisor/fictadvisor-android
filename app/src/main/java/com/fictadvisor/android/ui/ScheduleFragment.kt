package com.fictadvisor.android.ui

import android.os.Bundle
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
import com.fictadvisor.android.R

class ScheduleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        val timeColumn = view.findViewById<LinearLayout>(R.id.time_column)

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

        class Event(
            val type: String,
            val subject: String,
            val startTime: String,
            val endTime: String,
            val teacher: String
        ) {}

        val events = listOf(
            Event("Lecture", "Math", "8:30", "10:05", "Dr. Smith"),
            Event("Practice", "English", "10:25", "12:00", "Prof. Johnson"),
            Event("Lab", "Programming", "12:20", "13:55", "Mr. Smith")
        )

        val scheduleLayout = view.findViewById<ConstraintLayout>(R.id.schedule_layout)

        for (event in events) {
            val cardView = inflater.inflate(R.layout.card_view_template, null) as CardView
            cardView.id = View.generateViewId()

            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                dpToPx(calculateCardHeight(event.startTime, event.endTime))
            )
            cardView.layoutParams = layoutParams

            val textViewSubject = cardView.findViewById<TextView>(R.id.subject)
            textViewSubject.text = event.subject

            val textViewTime = cardView.findViewById<TextView>(R.id.time)
            textViewTime.text = "${event.startTime} - ${event.endTime}"

            val textViewTeacher = cardView.findViewById<TextView>(R.id.teacher)
            textViewTeacher.text = event.teacher

            val cardColor = when (event.type) {
                "Lecture" -> {
                    R.color.lecture_side_on
                }
                "Practice" -> {
                    R.color.practice_side_on
                }
                "Lab" -> {
                    R.color.lab_side_on
                }
                else -> {}
            }
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(),
                cardColor as Int
            ))

            val textContainer = cardView.findViewById<LinearLayout>(R.id.text_container)

            val linearColor = when (event.type) {
                "Lecture" -> {
                    R.color.lecture_main
                }
                "Practice" -> {
                    R.color.practice_main
                }
                "Lab" -> {
                    R.color.lab_main
                }
                else -> {}
            }
            textContainer.setBackgroundColor(ContextCompat.getColor(requireContext(),
                linearColor as Int
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

        return view
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