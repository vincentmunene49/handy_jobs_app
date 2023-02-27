package com.example.professionalapp.fragments.provideservices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.professionalapp.R
import com.example.professionalapp.databinding.FragmentAddProfessionBinding
import com.example.professionalapp.databinding.FragmentLoginBinding

class AddProfessionFragment:Fragment(R.layout.fragment_add_profession) {
    private var _binding: FragmentAddProfessionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProfessionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }

    override fun onResume() {
        super.onResume()
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout,categories)
        binding.spinnerItem.setAdapter(arrayAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}