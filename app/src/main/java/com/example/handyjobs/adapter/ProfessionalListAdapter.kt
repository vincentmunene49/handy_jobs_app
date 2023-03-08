package com.example.handyjobs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handyjobs.data.ProfessionCategory
import com.example.handyjobs.databinding.ProfessionalsListItemBinding

class ProfessionalListAdapter :
    RecyclerView.Adapter<ProfessionalListAdapter.ProfessionalListViewHolder>() {

    inner class ProfessionalListViewHolder(private val binding: ProfessionalsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(professional:ProfessionCategory){
                binding.tvName.text = professional.name
                binding.tvExperience.text = professional.experience
                professional.image.let {
                    Glide.with(itemView).load(it).into(binding.profileImage)
                }
            }
        }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<ProfessionCategory>() {
        override fun areItemsTheSame(
            oldItem: ProfessionCategory,
            newItem: ProfessionCategory
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProfessionCategory,
            newItem: ProfessionCategory
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessionalListViewHolder {
        return ProfessionalListViewHolder(
            ProfessionalsListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ProfessionalListViewHolder, position: Int) {
        val currentProfessional = differ.currentList[position]
        holder.bind(currentProfessional)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}