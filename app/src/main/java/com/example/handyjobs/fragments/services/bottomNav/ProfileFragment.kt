package com.example.handyjobs.fragments.services.bottomNav

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.handyjobs.R
import com.example.handyjobs.data.User
import com.example.handyjobs.databinding.FragmentProfileBinding
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.viewmodel.profileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<profileViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val image_uri = it.data?.data
                    image_uri?.let {
                        viewModel.uploadProfilePic(it)
                    }


                }

            }
        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getContent.launch(intent)
        }
        //setUserInformation
        setUserInfo()


    }

    private fun setUserInfo() {
        lifecycleScope.launchWhenStarted {
            viewModel.userInfo.collect {
                when (it) {
                    is ResultStates.Loading -> {
                        hideViews()

                    }
                    is ResultStates.Success -> {
                        binding.tvName.text = it.data?.name
                        Glide.with(requireContext()).load(it.data?.image)
                            .error(R.drawable.profile_icon).into(binding.profileImage)
                        showViews()
                    }
                    is ResultStates.Failure -> {
                        Log.d("Profile",it.message.toString())
                        showViews()
                    }
                    else -> Unit

                }
            }
        }

    }


    private fun hideViews() {
        binding.apply {
            tvName.visibility = View.INVISIBLE
            tvProfession.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showViews() {
        binding.apply {
            tvName.visibility = View.VISIBLE
            tvProfession.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}