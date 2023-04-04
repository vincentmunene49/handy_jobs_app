package com.example.professionalapp.fragments.provideservices

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
import com.example.professionalapp.data.Message
import com.example.professionalapp.data.TextMessage
import com.example.professionalapp.databinding.FragmentChatsBinding
import com.example.professionalapp.util.Results
import com.example.professionalapp.viewmodel.MessagesViewModel
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
    private val userArgs by navArgs<ChatsFragmentArgs>()
    private lateinit var fromid: String
    private lateinit var toId: String
    private lateinit var image: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(layoutInflater)
        toId = userArgs.id

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get current user{may be can be done in init of viewmodel, not sure}
        viewModel.getCurrentUser()
        //retrieve all messages
        viewModel.retrieveMessages(toId)
        //retrieve current user info

        lifecycleScope.launchWhenStarted {
            viewModel.user.collect { it ->
                when (it) {
                    is Results.Success -> {

                        it.data.let { user ->
                            image = user!!.image
                            chatsAdapter = ChatsAdapter(fromid, image, userArgs.senderData.image)
                            binding.rvChats.layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            binding.rvChats.adapter = chatsAdapter
                        }
                    }
                    is Results.Loading -> {

                    }
                    is Results.Failure -> {
                        Log.d("TAG", "${it.message.toString()}")

                    }
                    else -> Unit
                }
            }
        }

        fromid = viewModel.getCurrentUserId()
        binding.send.setOnClickListener {
            val text = binding.edText.text.toString()
            val message = Message(
                UUID.randomUUID().toString(),
                fromid,
                toId,
                TextMessage(text),
                System.currentTimeMillis() / 1000
            )
            viewModel.postMessage(toId, UUID.randomUUID().toString(), message)
            binding.edText.text?.clear()

        }

//        //get loading states
        lifecycleScope.launchWhenStarted {
            viewModel.messageList.collectLatest {
                when(it){
                    is Results.Success ->
                    {
                        Log.d("TAG","${it.data}")
                        chatsAdapter.differ.submitList(it.data)
                    }
                    is Results.Loading ->{

                    }
                    is Results.Failure ->{
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