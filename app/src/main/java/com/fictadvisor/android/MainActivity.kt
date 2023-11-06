package com.fictadvisor.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fictadvisor.android.data.dto.BaseResponse
import com.fictadvisor.android.viewmodel.AuthViewModel
import com.fictadvisor.android.viewmodel.AuthViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelFactory = AuthViewModelFactory()

        authViewModel = ViewModelProvider(this, viewModelFactory)[(AuthViewModel::class.java)]

        // test example
        authViewModel.login("DevTest", "dev122")

        authViewModel.authLoginResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    Log.d("MainActivity", "Loading...")
                }

                is BaseResponse.Success -> {
                    Log.d("MainActivity", "Success: ${it.data}")
                }

                is BaseResponse.Error -> {
                    Log.d("MainActivity", "Error: ${it.error}")
                }
                else -> {
                    // stopLoading()
                }
            }
        }
    }
}
