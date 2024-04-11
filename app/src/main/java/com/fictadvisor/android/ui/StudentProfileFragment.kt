package com.fictadvisor.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.fictadvisor.android.R
import com.fictadvisor.android.databinding.FragmentStudentProfileBinding
import com.fictadvisor.android.utils.StorageUtil



class StudentProfileFragment : Fragment() {
    private lateinit var binding: FragmentStudentProfileBinding
    private lateinit var storageUtil: StorageUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        storageUtil = StorageUtil(requireContext())
        val view = binding.root
        showUserData()

        binding.securityTab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_studentProfileFragment_to_securityStudentProfileFragment)
        }

        binding.buttonLogout.setOnClickListener {
            storageUtil.clearAll()
            activity?.finish()
        }


        return view
    }

    private fun showUserData() {
        if (storageUtil.getTokens() != null) {
            val student = storageUtil.getOrdinaryStudentInfo()
            if (student != null) {
                binding.name.text = String.format("%s %s", student.firstName, student.lastName)
                binding.groupNumber.text = student.group.code
                Glide.with(this).load(student.avatar).into(binding.avatar)
            }
        }
    }
}