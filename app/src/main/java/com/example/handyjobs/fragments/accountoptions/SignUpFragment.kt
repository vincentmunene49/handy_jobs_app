package com.example.handyjobs.fragments.accountoptions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.handyjobs.R
import com.example.handyjobs.data.User
import com.example.handyjobs.databinding.FragmentSignUpBinding
import com.example.handyjobs.util.AccountOptionsValidation
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class SignUpFragment:Fragment(R.layout.fragment_sign_up) {
    private var _binding:FragmentSignUpBinding?= null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegistrationViewModel>()

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
        binding.apply {
            registerButton.setOnClickListener {
                val user = User(
                    name.text.toString().trim(),
                    email.text.toString().trim(),
                    ""
                )
                val password = password.text.toString()
                viewModel.createAccount(user, password)
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }

        //check states
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is ResultStates.Loading ->{
                        //animation
                        binding.registerButton.startAnimation()
                    }
                    is ResultStates.Success ->{
                        binding.registerButton.revertAnimation()
                    }
                    is ResultStates.Failure ->{
                        Log.d("TAG",it.message.toString())
                        binding.registerButton.revertAnimation()
                    }
                    else -> Unit
                }//end of when
            }
        }

//validate
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{
                if(it.email is AccountOptionsValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.email.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if(it.password is AccountOptionsValidation.Failed){
                    withContext(Dispatchers.Main){
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