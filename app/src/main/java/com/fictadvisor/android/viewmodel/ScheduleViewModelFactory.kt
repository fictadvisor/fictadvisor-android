package com.fictadvisor.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fictadvisor.android.repository.ScheduleRepository

class ScheduleViewModelFactory(private val scheduleRepository: ScheduleRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(scheduleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}