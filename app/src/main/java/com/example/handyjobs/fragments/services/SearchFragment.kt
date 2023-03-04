package com.example.handyjobs.fragments.services

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.handyjobs.R
import com.example.handyjobs.databinding.FragmentSearchBinding
import com.example.handyjobs.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var categoriesList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        categoriesList = mutableListOf()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.requestFocus()

        //setup list view
        setUpListView()
        //search functionality

        val searchEditText =
            binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val filtered = filterCategories(s.toString())
                adapter.clear()
                adapter.addAll(filtered)
                adapter.notifyDataSetChanged()
            }
        })
//getselected item in listview

        binding.lvSearch.setOnItemClickListener { parent, view, position, id ->
            findNavController().navigate(R.id.action_searchFragment_to_bottomSheetView)


        }
        lifecycleScope.launchWhenStarted {
            viewModel.retrieveData.collectLatest {
                categoriesList = it
            }
        }


    }

    private fun filterCategories(query: String): List<String> {
        return categoriesList.let {
            it.filter { it.startsWith(query, ignoreCase = true) }
        }
    }

    private fun setUpListView() {
        adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoriesList)
        binding.lvSearch.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}