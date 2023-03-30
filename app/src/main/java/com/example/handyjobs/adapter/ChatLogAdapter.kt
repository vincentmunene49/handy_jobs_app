package com.example.handyjobs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.handyjobs.data.ProfessionCategory
import com.example.handyjobs.databinding.FragmentChatLogBinding

class ChatLogAdapter : RecyclerView.Adapter<ChatLogAdapter.ChatLogViewHolder>() {
    inner class ChatLogViewHolder(val binding:FragmentChatLogBinding):RecyclerView.ViewHolder(binding.root){}

    val diffUtil = object : DiffUtil.ItemCallback<ProfessionCategory>(){
        override fun areItemsTheSame(
            oldItem: ProfessionCategory,
            newItem: ProfessionCategory
        ): Boolean {
          return  oldItem.email == newItem.email
        }

        override fun areContentsTheSame(
            oldItem: ProfessionCategory,
            newItem: ProfessionCategory
        ): Boolean {
            return oldItem ==  newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatLogViewHolder {
        return ChatLogViewHolder(
            FragmentChatLogBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ChatLogViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }
}