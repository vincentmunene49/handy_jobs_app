package com.example.professionalapp.fragments.accountoptions


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.professionalapp.R
import com.example.professionalapp.activities.provideservices.ProvideServicesActivity
import com.example.professionalapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            Intent(requireContext(), ProvideServicesActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}