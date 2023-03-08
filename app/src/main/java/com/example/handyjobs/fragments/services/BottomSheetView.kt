package com.example.handyjobs.fragments.services

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.handyjobs.R
import com.example.handyjobs.databinding.ModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetView : BottomSheetDialogFragment(R.layout.modal_bottom_sheet) {
    private var _binding: ModalBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<BottomSheetViewArgs>()
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

        binding.tvTitle.text= title.toString()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}