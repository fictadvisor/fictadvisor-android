package com.fictadvisor.android.ui

import RegistrationViewModel
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.fictadvisor.android.R
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.data.dto.ExtendedGroupResponse
import com.fictadvisor.android.data.dto.GroupDTO
import com.fictadvisor.android.data.dto.OrdinaryStudentResponse
import com.fictadvisor.android.data.dto.user.ChangeInfoBody
import com.fictadvisor.android.data.dto.user.ChangeUserBody
import com.fictadvisor.android.data.dto.user.UserGroupState
import com.fictadvisor.android.databinding.FragmentGeneralBinding
import com.fictadvisor.android.databinding.FragmentStudentProfileBinding
import com.fictadvisor.android.repository.AuthRepository
import com.fictadvisor.android.repository.GroupRepository
import com.fictadvisor.android.repository.UserRepository
import com.fictadvisor.android.utils.StorageUtil
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory
import com.fictadvisor.android.viewmodel.GroupViewModel
import com.fictadvisor.android.viewmodel.GroupViewModelFactory
import com.fictadvisor.android.viewmodel.UserViewModel
import com.fictadvisor.android.viewmodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GeneralFragment : Fragment() {
    private lateinit var binding: FragmentGeneralBinding
    private lateinit var storageUtil: StorageUtil
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var registrationViewModel: RegistrationViewModel

    private val groupRepository = GroupRepository()
    private val groupsMap: MutableMap<String, String> = HashMap()
    private val groupCodesList: MutableList<String> = mutableListOf()
    private lateinit var userViewModel: UserViewModel
    private var authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private lateinit var authViewModel: AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralBinding.inflate(inflater, container, false)
        storageUtil = StorageUtil(requireContext())
        val view = binding.root

        groupViewModel = ViewModelProvider(
            this,
            GroupViewModelFactory(groupRepository)
        ).get(GroupViewModel::class.java)
        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)


        authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        ).get(AuthViewModel::class.java)
        showUserData()

        val token = authViewModel.refresh(storageUtil.getTokens()!!.accessToken)
        storageUtil.setTokens(token.toString(), storageUtil.getTokens()!!.refreshToken)


        binding.imageViewBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_generalFragment_to_studentProfileFragment)
        }

        binding.buttonEdite.setOnClickListener {
            editUserData()
            Log.i("Edit", "Edit")
        }

        return view
    }

    private fun editUserData() {
        val student = storageUtil.getOrdinaryStudentInfo()
        if (student != null) {
            val studentId = student.id
            val studentFirstName = binding.editTextTextName.text.toString()
            val studentMiddleName = binding.editTextTextFathername.text.toString()
            val studentLastName = binding.editTextTextLastname.text.toString()
            val studentEmail = student.email
            val studentUsername = student.username
            val studentState = student.group.state
            val studentGroupId = student.group.id
            val studentAvatar = student.avatar
            val studentTelegramId = student.telegramId
            val studentGroupCode = student.group.code
            val studentGropeRole = student.group.role


            val isValid = registrationViewModel.validateStudentData(
                studentFirstName,
                studentLastName,
                studentMiddleName,
                studentGroupId
            )
            if (!isValid) {
                return
            }

            val body = ChangeInfoBody(
                studentFirstName,
                studentLastName,
                studentMiddleName,
                studentGroupId,
                UserGroupState.valueOf(studentState)
                )

            val token = storageUtil.getTokens()!!.accessToken

            Log.i("Before update", token)

                userViewModel.changeInfo(
                token,
                studentId,
                body
            )

//            val body = ChangeUserBody(
//                studentEmail,
//                studentUsername,
//                UserGroupState.valueOf(student.group.state)
//            )
//
//            userViewModel.editUser(
//                storageUtil.getTokens()!!.accessToken,
//                studentId,
//                body
//            )

            userViewModel.changeInfoResponse.observe(viewLifecycleOwner) { response ->
                response?.let {
                    if (it is BaseResponse.Success) {
                        val student = it.data
                        val ordinaryStudentResponse = OrdinaryStudentResponse(
                            studentFirstName,
                            studentMiddleName,
                            studentLastName,
                            studentId,
                            studentUsername,
                            studentEmail,
                            studentAvatar,
                            studentTelegramId,
                            ExtendedGroupResponse(studentGroupId, studentGroupCode, studentState, studentGropeRole)
                        )
                        storageUtil.setOrdinaryStudentInfo(ordinaryStudentResponse)
                        showUserData()
                    }
                }
            }
        }
    }


    private fun showUserData() {
        if (storageUtil.getTokens() != null) {
            val student = storageUtil.getOrdinaryStudentInfo()
            if (student != null) {
                binding.editTextTextLastname.text = Editable.Factory.getInstance().newEditable(student.lastName)
                binding.editTextTextName.text = Editable.Factory.getInstance().newEditable(student.firstName)
                binding.editTextTextFathername.text = Editable.Factory.getInstance().newEditable(student.middleName)
                binding.editTextTextEmail.text = Editable.Factory.getInstance().newEditable(student.email)
                binding.editTextTextUsername.text = Editable.Factory.getInstance().newEditable(student.username)

                Glide.with(this).load(student.avatar).into(binding.avatar)
            }
        }
    }
}