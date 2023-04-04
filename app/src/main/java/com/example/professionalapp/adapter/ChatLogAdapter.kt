package com.example.handyjobs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.professionalapp.R
import com.example.professionalapp.data.SenderData
import com.example.professionalapp.databinding.ChatLogListItemsBinding
import com.example.professionalapp.databinding.FragmentChatLogBinding

class ChatLogAdapter : RecyclerView.Adapter<ChatLogAdapter.ChatLogViewHolder>() {
    inner class ChatLogViewHolder(val binding: ChatLogListItemsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(currentSender: SenderData?) {
            binding.tvSenderName.text = currentSender?.name
            currentSender?.image.let {
                Glide.with(itemView).load(it).placeholder(R.drawable.profile_icon).into(binding.senderProfileImage)
            }

        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<SenderData>(){
        override fun areItemsTheSame(
            oldItem: SenderData,
            newItem: SenderData
        ): Boolean {
          return  oldItem.email == newItem.email
        }

        override fun areContentsTheSame(
            oldItem: SenderData,
            newItem: SenderData
        ): Boolean {
            return oldItem ==  newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatLogViewHolder {
        return ChatLogViewHolder(
            ChatLogListItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ChatLogViewHolder, position: Int) {
        val currentSender = differ.currentList[position]
        holder.bind(currentSender)
        holder.itemView.setOnClickListener {
            onItemCLicked?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }
    var onItemCLicked:((Int)->Unit)? = null
}