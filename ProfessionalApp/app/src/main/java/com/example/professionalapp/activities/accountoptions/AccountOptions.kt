package com.example.professionalapp.activities.accountoptions


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.professionalapp.R
import com.example.professionalapp.databinding.ActivityAccountOptionsBinding
import dagger.hilt.android.AndroidEntryPoint

class AccountOptions : AppCompatActivity() {
    private lateinit var binding: ActivityAccountOptionsBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountOptionsBinding.inflate(layoutInflater)

        setContentView(
            binding
                .root
        )

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHost
        navController = navHost.navController
    }
}