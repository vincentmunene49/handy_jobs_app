package com.example.professionalapp.activities.provideservices

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.example.professionalapp.R
import com.example.professionalapp.databinding.ActivityProvideServicesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProvideServicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProvideServicesBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProvideServicesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.provide_services_navHost) as NavHost
        navController = navHost.navController

        binding.bottomNavView.setupWithNavController(navController)

    }

}
