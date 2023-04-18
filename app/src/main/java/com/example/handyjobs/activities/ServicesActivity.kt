package com.example.handyjobs.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.handyjobs.R
import com.example.handyjobs.databinding.ActivityServicesBinding
import com.example.handyjobs.util.ACTION_SHOW_CHAT_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServicesBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToChatLogFragment(intent)
        val navHost = supportFragmentManager.findFragmentById(R.id.services_navHost) as NavHost
        navController = navHost.navController

        //setUp bottomnav
        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.searchFragment || destination.id == R.id.bottomSheetView || destination.id == R.id.professionalProfileFragment || destination.id == R.id.chatsFragment) {
                binding.bottomNavView.visibility = View.GONE
            } else {
                binding.bottomNavView.visibility = View.VISIBLE
            }
        }


    }

    private fun navigateToChatLogFragment(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_CHAT_FRAGMENT) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.services_navHost) as NavHostFragment
            navHostFragment.findNavController().navigate(R.id.action_global_chatLog_fragment)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToChatLogFragment(intent)
    }
}