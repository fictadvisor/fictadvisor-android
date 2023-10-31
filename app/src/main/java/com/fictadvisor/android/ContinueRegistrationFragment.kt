package com.fictadvisor.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.fictadvisor.android.databinding.FragmentContinueRegistrationBinding

class ContinueRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentContinueRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContinueRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        val arguments = arguments
        if (arguments != null) {
//            val username = arguments.getString("username")
//            val name = arguments.getString("name")
//            val lastname = arguments.getString("lastname")
//            val middleName = arguments.getString("middleName")
//            val group = arguments.getString("group")
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(username: String, name: String, lastname: String, middleName: String, group: String): ContinueRegistrationFragment {
            val args = Bundle()
            args.putString("username", username)
            args.putString("name", name)
            args.putString("lastname", lastname)
            args.putString("middleName", middleName)
            args.putString("group", group)

            val fragment = ContinueRegistrationFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
