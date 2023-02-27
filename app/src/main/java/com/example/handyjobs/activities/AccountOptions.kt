package com.example.handyjobs.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.handyjobs.R
import com.example.handyjobs.databinding.ActivityAccountOptionsBinding
import com.example.handyjobs.databinding.ActivityServicesBinding
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountOptions : AppCompatActivity() {
    private lateinit var binding: ActivityAccountOptionsBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //navigate to home activity if user is logged in

        if (viewModel.checkIfUserIsLoggedIn()) {
            Intent(this,ServicesActivity::class.java).also {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)//this pops the previous activity so user
                // doesn't have to navigate back to login activity
                startActivity(it)
            }
        }
    }



}