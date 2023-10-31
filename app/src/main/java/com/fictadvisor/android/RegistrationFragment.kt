package com.fictadvisor.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.fictadvisor.android.databinding.FragmentRegistrationBinding

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
        return view
    }

    private fun onNextClicked() {
        val username = binding.editTextTextUsername.text.toString()
        val name = binding.editTextTextName.text.toString()
        val lastname = binding.editTextTextLastname.text.toString()
        val middleName = binding.editTextTextFathername.text.toString()
        val group = binding.editTextTextGroup.text.toString()

        val action = RegistrationFragmentDirections.actionRegistrationFragmentToContinueRegistrationFragment(
            username,
            name,
            lastname,
            middleName,
            group
        )

        Navigation.findNavController(requireView()).navigate(action)
    }

    companion object {
        @JvmStatic
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}