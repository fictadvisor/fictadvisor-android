package com.fictadvisor.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.fictadvisor.android.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.registerButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_registrationFragment)
        }

        binding.loginButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }

    }
}