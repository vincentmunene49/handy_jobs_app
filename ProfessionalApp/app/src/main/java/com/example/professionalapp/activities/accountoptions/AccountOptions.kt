package com.example.professionalapp.activities.accountoptions


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.professionalapp.R
import com.example.professionalapp.activities.provideservices.ProvideServicesActivity
import com.example.professionalapp.databinding.ActivityAccountOptionsBinding
import com.example.professionalapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AccountOptions : AppCompatActivity() {
    private lateinit var binding: ActivityAccountOptionsBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountOptionsBinding.inflate(layoutInflater)

        setContentView(
            binding
                .root
        )

        if (viewModel.checkIfUserIsLoggedIn()) {
            Intent(this,ProvideServicesActivity::class.java).also {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)//this pops the previous activity so user
                // doesn't have to navigate back to login activity
                startActivity(it)
            }
        }
    }
}