package com.example.handyjobs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handyjobs.R
import com.example.handyjobs.data.ProfessionCategory
import com.example.handyjobs.databinding.ChatLogListItemsBinding
import com.example.handyjobs.databinding.FragmentChatLogBinding

class ChatLogAdapter : RecyclerView.Adapter<ChatLogAdapter.ChatLogViewHolder>() {
    inner class ChatLogViewHolder(val binding: ChatLogListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentProfessional: ProfessionCategory?) {
            currentProfessional.let {
                binding.tvSenderName.text = it?.name
                Glide.with(itemView).load(it?.image).placeholder(R.drawable.profile_icon)
                    .into(binding.senderProfileImage)

            }

        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ProfessionCategory>() {
        override fun areItemsTheSame(
            oldItem: ProfessionCategory,
            newItem: ProfessionCategory
        ): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(
            oldItem: ProfessionCategory,
            newItem: ProfessionCategory
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatLogViewHolder {
        return ChatLogViewHolder(
            ChatLogListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatLogViewHolder, position: Int) {
        val currentProfessional = differ.currentList[position]
        holder.bind(currentProfessional)

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClicked:((Int)-> Unit)? = null
}