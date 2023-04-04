package com.example.handyjobs.fragments.services.bottomNav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handyjobs.adapter.ChatsAdapter
import com.example.handyjobs.data.TextMessage
import com.example.handyjobs.databinding.FragmentChatsBinding
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.viewmodel.MessagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatsAdapter: ChatsAdapter
    private val viewModel by viewModels<MessagesViewModel>()
    private val profArgs by navArgs<ChatsFragmentArgs>()
    private lateinit var fromid: String
    private lateinit var toId: String
    private lateinit var image:String

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

        //get current user
        viewModel.getCurrentUser()
        //retrieve current user

        lifecycleScope.launchWhenStarted {
            viewModel.user.collect{ it ->
                when(it){
                    is ResultStates.Success ->
                    {

                        it.data.let { user ->
                            image = user!!.image
                            chatsAdapter = ChatsAdapter(fromid,image,profArgs.professional.image)
                            binding.rvChats.layoutManager =
                                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                            binding.rvChats.adapter = chatsAdapter
                        }
                    }
                    is ResultStates.Loading ->{

                    }
                    is ResultStates.Failure ->{
                        Log.d("TAG","${it.message.toString()}")

                    }else -> Unit
                }
            }
        }


//get receivers id
        viewModel.retrieveProfessionalId(profArgs.professional.email, onSuccess = {
            toId = it
            viewModel.retrieveMessages(it)
        }, onFailure = {
            Log.d("ID", it.message.toString())

        })
        fromid = viewModel.getCurrentUserId()
        binding.send.setOnClickListener {
            val text = binding.edText.text.toString()
            val message = com.example.handyjobs.data.Message(
                UUID.randomUUID().toString(),
                fromid,
                toId,
                TextMessage(text),
                System.currentTimeMillis() / 1000
            )
            viewModel.postMessage(toId, UUID.randomUUID().toString(),message)
            binding.edText.text?.clear()

        }
        //get loading states
        lifecycleScope.launchWhenStarted {
            viewModel.messageList.collectLatest {
                when(it){
                    is ResultStates.Success ->
                    {
                        Log.d("TAG","${it.data}")

                        chatsAdapter.differ.submitList(it.data)
                    }
                    is ResultStates.Loading ->{

                    }
                    is ResultStates.Failure ->{
                        Log.d("TAG","${it.message.toString()}")

                    }else -> Unit
                }
            }
        }



    }


//    //setup recycler view
//    private fun setUpRecylerView() {
//        chatsAdapter = ChatsAdapter(toId,image)
//        binding.rvChats.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        binding.rvChats.adapter = chatsAdapter
//
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}