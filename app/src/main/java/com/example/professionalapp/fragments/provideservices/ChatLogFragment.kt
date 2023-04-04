package com.example.professionalapp.fragments.provideservices

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handyjobs.adapter.ChatLogAdapter
import com.example.professionalapp.R
import com.example.professionalapp.databinding.FragmentChatLogBinding
import com.example.professionalapp.util.Results
import com.example.professionalapp.viewmodel.MessagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ChatLogFragment : Fragment(R.layout.fragment_chats) {
    private var _binding: FragmentChatLogBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MessagesViewModel>()
    private lateinit var chatLogAdapter: ChatLogAdapter
    private lateinit var toId: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatLogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        //retrieve receivers id then navigate
        chatLogAdapter.onItemCLicked = {
            val clickedUser = chatLogAdapter.differ.currentList[it]
            viewModel.getReceiverId(clickedUser.email, { to_id ->
                toId = to_id
                lifecycleScope.launch(Dispatchers.Main) {
                    val action = ChatLogFragmentDirections.actionChatLogFragmentToChatsFragment(
                        clickedUser,
                        toId
                    )
                    findNavController().navigate(action)
                }
            }, { e ->
                Log.d("ID", e.message.toString())
            })
        }
        lifecycleScope.launchWhenStarted {
            viewModel.app_users.collect {
                when (it) {
                    is Results.Success -> {
                        chatLogAdapter.differ.submitList(it.data)

                    }
                    is Results.Loading -> {

                    }
                    is Results.Failure -> {

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        chatLogAdapter = ChatLogAdapter()
        binding.rvChatLog.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = chatLogAdapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}