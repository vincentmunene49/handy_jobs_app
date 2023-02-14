package com.example.handyjobs.fragments.accountoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.handyjobs.R
import com.example.handyjobs.databinding.FragmentSignUpBinding

class SignUpFragment:Fragment(R.layout.fragment_sign_up) {
    private var _binding:FragmentSignUpBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvToLogin.setOnClickListener{
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}