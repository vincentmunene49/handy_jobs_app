package com.example.professionalapp.fragments.accountoptions


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope

import com.example.professionalapp.R
import com.example.professionalapp.activities.provideservices.ProvideServicesActivity
import com.example.professionalapp.databinding.FragmentLoginBinding
import com.example.professionalapp.util.AccountOptionsValidation
import com.example.professionalapp.util.Results
import com.example.professionalapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

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
        binding.apply {

            loginButton.setOnClickListener {
                val email = email.text.toString().trim()
                val password = password.text.toString()
                viewModel.signInUser(email, password)
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Results.Success -> {
                        binding.loginButton.revertAnimation()
                        Intent(requireActivity(), ProvideServicesActivity::class.java).also {
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)//this pops the previous activity so user
                            // doesn't have to navigate back to login activity
                            startActivity(it)
                        }
                    }
                    is Results.Failure -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                        binding.loginButton.revertAnimation()
                    }
                    is Results.Loading -> {
                        binding.loginButton.startAnimation()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                if (it.email is AccountOptionsValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.email.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if (it.password is AccountOptionsValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.password.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}