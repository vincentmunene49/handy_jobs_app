package com.example.professionalapp.activities.provideservices

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.professionalapp.R
import com.example.professionalapp.databinding.ActivityProvideServicesBinding
import com.example.professionalapp.util.ACTION_SHOW_CHAT_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProvideServicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProvideServicesBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProvideServicesBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navigateToChatLogFragment(intent)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.provide_services_navHost) as NavHost
        navController = navHost.navController

        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.chatsFragment) {
                binding.bottomNavView.visibility = View.GONE
            } else {
                binding.bottomNavView.visibility = View.VISIBLE
            }

        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToChatLogFragment(intent)
    }

    private fun navigateToChatLogFragment(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_CHAT_FRAGMENT) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.provide_services_navHost) as NavHostFragment
            navHostFragment.findNavController().navigate(R.id.action_global_chatLog_fragment)
        }
    }

}
