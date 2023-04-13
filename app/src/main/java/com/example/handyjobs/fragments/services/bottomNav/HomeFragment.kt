package com.example.handyjobs.fragments.services.bottomNav

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.handyjobs.R
import com.example.handyjobs.adapter.PopularCategoriesAdapter
import com.example.handyjobs.databinding.FragmentHomeBinding
import com.example.handyjobs.services.FirebaseMessagingService
import com.example.handyjobs.util.CheckPermissions
import com.example.handyjobs.util.REQUEST_LOCATION_PERMISSION_CODE
import com.example.handyjobs.util.REQUEST_NOTIFICATION_CODE
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.viewmodel.HomeViewModel
import com.example.handyjobs.viewmodel.LoginViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel by viewModels<HomeViewModel>()
    private val login_viewModel by viewModels<LoginViewModel>()
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var popularCategoriesAdapter: PopularCategoriesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //request permissions
        requestPermissions()
        requestNotificationPermission()

        //update token
        updateToken()

        //navigate to searchview
        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        //set up recylerview
        setUpRecylerView()
        //set up map
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        lifecycleScope.launchWhenStarted {
            viewModel.retrieveData.collectLatest {
                if (it.isNotEmpty()) {
                    popularCategoriesAdapter.differ.submitList(it)
                }
            }
        }
        //screen states

        lifecycleScope.launchWhenStarted {
            viewModel.retrievedState.collect {
                when (it) {
                    is ResultStates.Success -> {
                        hideLoading()
                    }
                    is ResultStates.Loading -> {
                        showLoading()

                    }
                    is ResultStates.Failure -> {
                        hideLoading()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> Unit
                }
            }
        }


    }

    private fun showLoading() {
        binding.progressBar.apply {
            visibility = View.VISIBLE

        }

    }

    private fun hideLoading() {
        binding.progressBar.apply {
            visibility = View.GONE
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }

    }

    private fun setUpRecylerView() {

        popularCategoriesAdapter = PopularCategoriesAdapter()
        binding.rvPopularCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = popularCategoriesAdapter
        }
    }

    private fun requestPermissions() {
        if (CheckPermissions.hasPermissions(requireContext()))
            return
        EasyPermissions.requestPermissions(
            this,
            "This App cannot work properly without these permissions",
            REQUEST_LOCATION_PERMISSION_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    }

    private fun requestNotificationPermission() {
        if (CheckPermissions.hasNotificationPermission(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                "This app may not work properly without these permissions",
                REQUEST_NOTIFICATION_CODE,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }


    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    @Suppress("deprecation")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //update token
    private fun updateToken() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                if (token !=null) {
                    withContext(Dispatchers.Main) {
                        login_viewModel.updateToken(token)
                    }
                }
            } catch (e: Exception) {
                Log.d("TOKEN", e.message.toString())
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}