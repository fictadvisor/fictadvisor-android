package com.fictadvisor.android.ui

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.fictadvisor.android.R
import com.fictadvisor.android.data.dto.OrdinaryStudentResponse
import com.fictadvisor.android.databinding.FragmentGeneralBinding
import com.fictadvisor.android.databinding.FragmentStudentProfileBinding
import com.fictadvisor.android.utils.StorageUtil


class GeneralFragment : Fragment() {
    private lateinit var binding: FragmentGeneralBinding
    private lateinit var storageUtil: StorageUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralBinding.inflate(inflater, container, false)
        storageUtil = StorageUtil(requireContext())
        val view = binding.root

        showUserData()

        binding.imageViewBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_generalFragment_to_studentProfileFragment)
        }
        return view
    }

    private fun showUserData() {
        if (storageUtil.getTokens() != null) {
            val student = storageUtil.getOrdinaryStudentInfo()
            if (student != null) {
                binding.editTextTextLastname.text = Editable.Factory.getInstance().newEditable(student.lastName)
                binding.editTextTextName.text = Editable.Factory.getInstance().newEditable(student.firstName)
                binding.editTextTextFathername.text = Editable.Factory.getInstance().newEditable(student.middleName)
                Glide.with(this).load(student.avatar).into(binding.avatar)
            }
        }
    }

}