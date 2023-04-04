package com.example.handyjobs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.handyjobs.R
import com.example.handyjobs.data.Message
import com.example.handyjobs.databinding.FragmentChatsBinding
import com.example.handyjobs.databinding.FromListItemsBinding
import com.example.handyjobs.databinding.ToListItemsBinding

class ChatsAdapter(private val fromId: String, private val sender_image:String?, private val receiver_image:String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ChatsViewHolder(private val binding: FragmentChatsBinding) :
        RecyclerView.ViewHolder(binding.root)

    //viewHolder for From message
    inner class MessageFromViewHolder(val binding: FromListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message?) {
            binding.tvFromTextMessage.text = message?.text?.text.toString()
            sender_image.let {
                Glide.with(itemView).load(it).placeholder(R.drawable.profile_icon).into(binding.profileImage)
            }
        }

    }

    //viewHolder for to Message
    inner class MessageToViewHolder(val binding: ToListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message?) {
            binding.tvToTextView.text = message?.text?.text.toString()
            receiver_image.let {
                Glide.with(itemView).load(it).placeholder(R.drawable.profile_icon).into(binding.profileImage)
            }


        }

    }

    //companion object to identify different viewTypes
    companion object {
        const val VIEW_TYPE_MESSAGE_FROM = 0
        const val VIEW_TYPE_MESSAGE_TO = 1
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MESSAGE_FROM -> {
                val view =
                    FromListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MessageFromViewHolder(view)
            }
            VIEW_TYPE_MESSAGE_TO -> {
                val view =
                    ToListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MessageToViewHolder(view)

            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = differ.currentList[position]
        when (holder) {
            is MessageFromViewHolder -> {
                holder.bind(message)
            }
            is MessageToViewHolder -> {
                holder.bind(message)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = differ.currentList[position]
        return if (message.fromId == fromId) VIEW_TYPE_MESSAGE_FROM else VIEW_TYPE_MESSAGE_TO
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }




}