package com.fictadvisor.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fictadvisor.android.R
import com.fictadvisor.android.databinding.FragmentScheduleBinding
import com.fictadvisor.android.utils.StorageUtil

class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
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
        val userData = storageUtil.getOrdinaryStudentInfo()
        Glide.with(this).load(userData?.avatar).apply(RequestOptions.circleCropTransform()).into(binding.imageButton)
        binding.imageButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_scheduleFragment_to_studentProfileFragment)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): ScheduleFragment {
            return ScheduleFragment()
        }

    }
}