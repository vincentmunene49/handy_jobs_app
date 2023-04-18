package com.example.handyjobs.fragments.services.bottomNav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handyjobs.R
import com.example.handyjobs.Retrofit.RetrofitInstance
import com.example.handyjobs.adapter.ChatsAdapter
import com.example.handyjobs.data.*
import com.example.handyjobs.databinding.FragmentChatsBinding
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.viewmodel.MessagesViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    private lateinit var image: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(layoutInflater)

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
//            val bundle = Bundle().apply {
//                putParcelable("professional",profArgs.professional)
//            }
//            findNavController().navigate(R.id.action_chatsFragment_to_chatLogFragment,bundle)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get current user
        viewModel.getCurrentUser()
        //retrieve current user

        lifecycleScope.launchWhenStarted {
            viewModel.user.collect { it ->
                when (it) {
                    is ResultStates.Success -> {

                        it.data.let { user ->
                            image = user!!.image
                            chatsAdapter = ChatsAdapter(fromid, image, profArgs.professional.image)
                            binding.rvChats.layoutManager = LinearLayoutManager(
                                requireContext(), LinearLayoutManager.VERTICAL, false
                            )
                            binding.rvChats.adapter = chatsAdapter
                        }
                    }
                    is ResultStates.Loading -> {

                    }
                    is ResultStates.Failure -> {
                        Log.d("TAG", it.message.toString())

                    }
                    else -> Unit
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
            if (binding.edText.text.toString().isEmpty()) return@setOnClickListener
            val text = binding.edText.text.toString()
            val message = com.example.handyjobs.data.Message(
                UUID.randomUUID().toString(),
                fromid,
                toId,
                TextMessage(text),
                System.currentTimeMillis() / 1000
            )
            val notification = profArgs.professional.token?.let { token ->
                PushNotification(
                    NotificationData("New Message", text),
                    token
                )
            }
            viewModel.postMessage(toId, UUID.randomUUID().toString(), message)
            //send push notification
            if (notification != null) {
                sendNotification(notification)
            }
            binding.edText.text?.clear()

        }
        //get loading states
        lifecycleScope.launchWhenStarted {
            viewModel.messageList.collectLatest {
                when (it) {
                    is ResultStates.Success -> {
                        Log.d("TAG", "${it.data}")

                        chatsAdapter.differ.submitList(it.data)
                        binding.rvChats.scrollToPosition(chatsAdapter.itemCount - 1)

                    }
                    is ResultStates.Loading -> {

                    }
                    is ResultStates.Failure -> {
                        Log.d("TAG", it.message.toString())

                    }
                    else -> Unit
                }
            }
        }


    }


    //function to send the notification
    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("RETROFIT", Gson().toJson(response))
                    Log.d("NOTIFI", "${Gson().toJson(notification)}")

                } else {
                    Log.d("RETROFIT", "error ${response.raw()}")

                }

            } catch (e: Exception) {
                Log.d("RETROFIT", e.message.toString())
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