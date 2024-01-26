package com.fictadvisor.android

import com.fictadvisor.android.services.TelegramService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.GroupDTO
import com.fictadvisor.android.databinding.FragmentRegistrationBinding
import com.fictadvisor.android.repository.GroupRepository
import com.fictadvisor.android.validator.InputValidator
import com.fictadvisor.android.viewmodel.GroupViewModel
import com.fictadvisor.android.viewmodel.GroupViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var groupViewModel: GroupViewModel
    private val groupRepository = GroupRepository()
    private val groupsMap: MutableMap<String, String> = HashMap()
    private val groupCodesList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        groupViewModel = ViewModelProvider(
            this,
            GroupViewModelFactory(groupRepository)
        ).get(GroupViewModel::class.java)

        getAllGroups()
        setGroupsAdapter()
        setValidationOfGroupCode()

        binding.buttonNext.setOnClickListener {
            onNextClicked()
        }

        binding.buttonAddTelegram.setOnClickListener {
            TelegramService(requireContext()).openTelegramBot()
        }
        return view
    }

    private fun setValidationOfGroupCode() {
        val actv = binding.groupACTV
        actv.addTextChangedListener {
            if (groupCodesList.find { it.contentEquals(actv.text.toString()) } == null) {
                actv.error = "Невідомий шифр групи"
            } else {
                actv.error = null
            }
        }
    }

    private fun setGroupsAdapter() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, groupCodesList)

        val actv: AutoCompleteTextView = binding.groupACTV
        actv.setAdapter(adapter)
    }

    private fun getAllGroups() {
        CoroutineScope(Dispatchers.IO).launch {
            groupViewModel.getAllGroups()
        }

        groupViewModel.getAllGroupsResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it is BaseResponse.Success) {
                    val groupsResponseList = it.data?.groups!!
                    for (group: GroupDTO in groupsResponseList) {
                        groupsMap[group.code] = group.id
                        groupCodesList.add(group.code)
                    }
                }
            }
        }
    }

    private fun onNextClicked() {
        val username = binding.editTextTextUsername.text.toString()
        val name = binding.editTextTextName.text.toString()
        val lastname = binding.editTextTextLastname.text.toString()
        val middleName = binding.editTextTextFathername.text.toString()
        val group = groupsMap[binding.groupACTV.text.toString()]

        if (group == null || !isInputValid(username, name, lastname, middleName, group)) {
            return
        }

        val action = RegistrationFragmentDirections.actionRegistrationFragmentToContinueRegistrationFragment(
            username,
            name,
            lastname,
            middleName,
            group
        )

        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun isInputValid(username:String, name:String, lastname:String, middleName:String, group:String): Boolean {
        val usernameValidationResult = InputValidator.isUsernameValid(username)
        val nameValidationResult = InputValidator.isNameValid(name)
        val lastnameValidationResult = InputValidator.isLastnameValid(lastname)
        val middleNameValidationResult = InputValidator.isMiddleNameValid(middleName)
        val groupValidationResult = InputValidator.isGroupValid(group)

        if (!usernameValidationResult.isValid) {
            Toast.makeText(requireContext(), usernameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!nameValidationResult.isValid) {
            Toast.makeText(requireContext(), nameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!lastnameValidationResult.isValid) {
            Toast.makeText(requireContext(), lastnameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!middleNameValidationResult.isValid) {
            Toast.makeText(requireContext(), middleNameValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!groupValidationResult.isValid) {
            Toast.makeText(requireContext(), groupValidationResult.errorMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    companion object {
        @JvmStatic
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}