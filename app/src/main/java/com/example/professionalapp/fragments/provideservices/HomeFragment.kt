package com.example.professionalapp.fragments.provideservices

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.professionalapp.R
import com.example.professionalapp.databinding.FragmentHomeBinding
import com.example.professionalapp.util.CheckPermissions
import com.example.professionalapp.util.REQUSET_LOCATION_PERMISSION_CODE
import com.example.professionalapp.viewmodel.LoginViewModel
import com.example.professionalapp.viewmodel.receiveLocationUpdates
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home),OnMapReadyCallback,EasyPermissions.PermissionCallbacks {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap:GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private  val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //request permission
        requestPermissions()


        //update token
        updateToken()

        //set up map
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //update the location
        //receiveLocationUpdates(requireContext())?.let { viewModel.updateLocation(it) }
        receiveLocationUpdates(requireContext()) { geoPoint ->
            viewModel.updateLocationandStatus(geoPoint)
            val currentLatLng = LatLng(geoPoint.latitude, geoPoint.longitude)
            mMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
        }

        //Toast.makeText(requireContext(),"${receiveLocationUpdates(requireContext())!!.latitude}",Toast.LENGTH_LONG).show()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

          }
    private fun requestPermissions() {
        if (CheckPermissions.hasPermissions(requireContext()))
            return
        EasyPermissions.requestPermissions(
            this,
            "This App cannot work properly without these permissions",
            REQUSET_LOCATION_PERMISSION_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    }

    //update token
    private fun updateToken() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                if (token !=null) {
                    withContext(Dispatchers.Main) {
                        viewModel.updateToken(token)
                    }
                }
            } catch (e: Exception) {
                Log.d("TOKEN", e.message.toString())
            }
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
}