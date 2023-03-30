package com.example.handyjobs.fragments.services.bottomNav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.handyjobs.R
import com.example.handyjobs.adapter.ChatsAdapter
import com.example.handyjobs.databinding.FragmentChatLogBinding
import com.example.handyjobs.databinding.FragmentChatsBinding

class ChatsLogFragment:Fragment(R.layout.fragment_chat_log) {
    private var _binding:FragmentChatLogBinding? = null
    private val binding get() = _binding!!
    private lateinit var ChatsAdapter: ChatsAdapter

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

        binding.toChats.setOnClickListener {
            findNavController().navigate(R.id.action_chatLogFragment_to_chatsFragment)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}