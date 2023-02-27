package com.example.professionalapp.fragments.provideservices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.professionalapp.R
import com.example.professionalapp.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment: Fragment(R.layout.fragment_home),OnMapReadyCallback {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapFragment: SupportMapFragment

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

        //set up map
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        val sydney = LatLng(0.0060, 34.5974)
        val marker =  googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Maseno")
        )
        if (marker != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position,10f))
        }    }
}