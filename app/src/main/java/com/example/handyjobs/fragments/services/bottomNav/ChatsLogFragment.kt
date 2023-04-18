package com.example.handyjobs.fragments.services.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handyjobs.R
import com.example.handyjobs.adapter.ChatLogAdapter
import com.example.handyjobs.adapter.ChatsAdapter
import com.example.handyjobs.data.ProfessionCategory
import com.example.handyjobs.databinding.FragmentChatLogBinding
import com.example.handyjobs.viewmodel.DbViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsLogFragment : Fragment(R.layout.fragment_chat_log) {
    private var _binding: FragmentChatLogBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatsLogAdapter: ChatLogAdapter
    private val dbViewModel by viewModels<DbViewModel>()


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

        //setUpAdapter
        setUpRecyclerView()



        chatsLogAdapter.onItemClicked = { position ->
            val clickedUser = chatsLogAdapter.differ.currentList[position]
            val action = ChatsLogFragmentDirections.actionChatLogFragmentToChatsFragment(clickedUser)
            findNavController().navigate(action)
        }

        lifecycleScope.launchWhenStarted {
            dbViewModel.professionalsList.collect{
                it.let {
                    chatsLogAdapter.differ.submitList(it)
                }

            }
        }

    }

    private fun setUpRecyclerView() {
        chatsLogAdapter = ChatLogAdapter()
        binding.rvChatLog.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = chatsLogAdapter
        }

    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}