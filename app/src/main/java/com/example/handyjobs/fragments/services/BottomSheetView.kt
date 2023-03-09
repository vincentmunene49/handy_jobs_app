package com.example.handyjobs.fragments.services

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.handyjobs.R
import com.example.handyjobs.adapter.ProfessionalListAdapter
import com.example.handyjobs.databinding.ModalBottomSheetBinding
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.viewmodel.RetrieveProfessionalViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BottomSheetView : BottomSheetDialogFragment(R.layout.modal_bottom_sheet) {
    private var _binding: ModalBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<BottomSheetViewArgs>()
    private lateinit var professionalListAdapter: ProfessionalListAdapter
    private val viewModel by viewModels<RetrieveProfessionalViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ModalBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = args.title

        binding.tvTitle.text = title.title
        setUpRecyclerView()

        viewModel.getDocumentIds(title.title)

        lifecycleScope.launchWhenStarted {
            viewModel.professionals.collect {
                professionalListAdapter.differ.submitList(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.screenState.collectLatest {
                when (it) {
                    is ResultStates.Loading -> {
                        showLoading()

                    }
                    is ResultStates.Success -> {
                        hideLoading()

                    }
                    is ResultStates.Failure -> {
                        hideLoading()
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setUpRecyclerView() {
        binding.rvProfessionals.apply {
            professionalListAdapter = ProfessionalListAdapter()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = professionalListAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        // Set the peek height of the bottom sheet to half of the screen height
        val windowHeight = Resources.getSystem().displayMetrics.heightPixels
        dialog?.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.apply {
                layoutParams = layoutParams.apply {
                    height = windowHeight / 2
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}