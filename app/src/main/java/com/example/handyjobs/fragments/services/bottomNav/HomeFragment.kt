package com.example.handyjobs.fragments.services.bottomNav

import android.os.Bundle
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
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.viewmodel.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnMapReadyCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var popularCategoriesAdapter: PopularCategoriesAdapter
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

        //navigate to searchview
        binding.searchView.setOnClickListener{
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
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_LONG).show()
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
        val sydney = LatLng(0.0060, 34.5974)
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Maseno")
        )
        if (marker != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 10f))
        }
    }

    private fun setUpRecylerView() {

        popularCategoriesAdapter = PopularCategoriesAdapter()
        binding.rvPopularCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = popularCategoriesAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}