package com.example.handyjobs.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.example.handyjobs.R
import com.example.handyjobs.databinding.ActivityServicesBinding

class ServicesActivity:AppCompatActivity() {
    private lateinit var binding:ActivityServicesBinding
private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.services_navHost) as NavHost
        navController = navHost.navController

        //setUp bottomnav
        binding.bottomNavView.setupWithNavController(navController)

    }
}