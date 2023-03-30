package com.example.handyjobs.fragments.services.bottomNav

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handyjobs.adapter.ChatsAdapter
import com.example.handyjobs.data.TextMessage
import com.example.handyjobs.databinding.FragmentChatsBinding

const val userId = "vin"

class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var ChatsAdapter: ChatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //dummy data to simulate
        val toMessage = com.example.handyjobs.data.Message(
            "1",
            "from",
            userId,
            TextMessage("This is From the receiver. Can i have some pizza meeen"),
            System.currentTimeMillis()
        )

        val fromMessage = com.example.handyjobs.data.Message(
            "2",
            userId,
            "from",
            TextMessage("This is From sender. What can i serve you today"),
            System.currentTimeMillis()
        )
        setUpRecylerView()
        val listOfMessages: List<com.example.handyjobs.data.Message> = listOf(
            fromMessage,
            toMessage
        )
        ChatsAdapter.differ.submitList(listOfMessages)
    }

    //setup recycler view
    private fun setUpRecylerView() {
        ChatsAdapter = ChatsAdapter(userId)
        binding.rvChats.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvChats.adapter = ChatsAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}