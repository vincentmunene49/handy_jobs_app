package com.example.handyjobs.fragments.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.handyjobs.R
import com.example.handyjobs.databinding.FragmentProfProfileBinding
import com.example.handyjobs.viewmodel.DbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfessionalProfileFragment : Fragment(R.layout.fragment_prof_profile) {
    private var _binding: FragmentProfProfileBinding? = null
    private val binding get() = _binding!!
    private val dbViewModel by viewModels<DbViewModel>()
    private val profArgs by navArgs<ProfessionalProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set up professional view
        setUpProfessional()
        //contact professional
        binding.contact.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("professional",profArgs.professionalDetails)
            }
            lifecycleScope.launch {
                dbViewModel.insertProfessional(profArgs.professionalDetails)
            }
            findNavController().navigate(R.id.action_professionalProfileFragment_to_chatsFragment,bundle)
        }
    }

    private fun setUpProfessional() {
        binding.tvName.text = profArgs.professionalDetails.name
        binding.tvProfession.text = "${profArgs.professionalDetails.experience} +years"
        binding.tvSkillDescription.text = profArgs.professionalDetails.description
       Glide.with(binding.root).load(profArgs.professionalDetails.image).placeholder(R.drawable.profile_icon).into(binding.profileImage)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}