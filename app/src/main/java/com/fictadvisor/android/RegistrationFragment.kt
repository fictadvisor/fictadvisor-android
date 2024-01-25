package com.fictadvisor.android

import com.fictadvisor.android.services.TelegramService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.fictadvisor.android.databinding.FragmentRegistrationBinding
import com.fictadvisor.android.validator.InputValidator

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding

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

        binding.buttonNext.setOnClickListener {
            onNextClicked()
        }


        binding.buttonAddTelefram.setOnClickListener {
            TelegramService(requireContext()).openTelegramBot()
        }
        return view
    }

    private fun onNextClicked() {
        val username = binding.editTextTextUsername.text.toString()
        val name = binding.editTextTextName.text.toString()
        val lastname = binding.editTextTextLastname.text.toString()
        val middleName = binding.editTextTextFathername.text.toString()

        //get groups list from server and set it to spinner
        val group = binding.spinnerGroup.toString()

        if (!isInputValid(username, name, lastname, middleName, group)) {
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