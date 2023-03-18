package com.example.professionalapp.fragments.provideservices

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.professionalapp.R
import com.example.professionalapp.data.ProfessionUpload
import com.example.professionalapp.databinding.FragmentAddProfessionBinding
import com.example.professionalapp.util.Results
import com.example.professionalapp.util.UploadSKillsFieldStates
import com.example.professionalapp.util.Validation
import com.example.professionalapp.viewmodel.AddProfessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddProfessionFragment : Fragment(R.layout.fragment_add_profession) {
    private var _binding: FragmentAddProfessionBinding? = null
    private val binding get() = _binding!!
    private lateinit var userId: String
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var selectedItem: String = ""
    private val viewModel by viewModels<AddProfessionViewModel>()

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

        binding.spinnerItem.setOnItemClickListener { parent, view, position, id ->
            selectedItem = arrayAdapter.getItem(position).toString()

        }
        binding.postSkills.setOnClickListener {
            val professionUpload = ProfessionUpload(
                selectedItem,
                binding.skill.text.toString().trim(),
                binding.experience.text.toString().trim(),
                description = binding.description.text.toString().trim()
            )

            viewModel.uploadProfession(professionUpload)
        }
////collectId
//        lifecycleScope.launchWhenStarted {
//            viewModel.emitId.collect {
//                userId = it
//            }
//        }
//upload skills
        lifecycleScope.launchWhenStarted {
            viewModel.upload.collect {
                when (it) {
                    is Results.Loading -> {
                        binding.postSkills.startAnimation()
                    }
                    is Results.Success -> {
                        binding.postSkills.revertAnimation()
                        resetScreen()
                    }
                    is Results.Failure -> {
                        binding.postSkills.revertAnimation()
                        Log.d("UPLOAD", it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        //validate
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                if (it.category is Validation.Failure) {
                    withContext(Dispatchers.Main) {
                        binding.spinnerItem.apply {
                            requestFocus()
                            error = it.category.message
                        }
                    }
                }
                if (it.skill_name is Validation.Failure) {
                    withContext(Dispatchers.Main) {
                        binding.spinnerItem.apply {
                            requestFocus()
                            error = it.skill_name.message
                        }
                    }
                }
                if (it.experience is Validation.Failure) {
                    withContext(Dispatchers.Main) {
                        binding.spinnerItem.apply {
                            requestFocus()
                            error = it.experience.message
                        }
                    }
                }
                if (it.description is Validation.Failure) {
                    withContext(Dispatchers.Main) {
                        binding.spinnerItem.apply {
                            requestFocus()
                            error = it.description.message
                        }
                    }
                }
            }
        }
    }

    private fun resetScreen() {
        binding.apply {
            spinnerItem.apply {
                clearFocus()
                error = null
                setText("")
            }
            skill.apply {
                clearFocus()
                error = null
                setText("")
            }
            experience.apply {
                clearFocus()
                error = null
                setText("")
            }
            upload.apply {
                clearFocus()
                error = null
                setText("")
            }
            description.apply {
                clearFocus()
                error = null
                setText("")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val categories = resources.getStringArray(R.array.categories)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, categories)
        binding.spinnerItem.setAdapter(arrayAdapter)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}