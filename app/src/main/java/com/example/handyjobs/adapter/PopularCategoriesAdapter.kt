package com.example.handyjobs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.handyjobs.R
import com.example.handyjobs.databinding.ActivityServicesBinding
import com.example.handyjobs.databinding.CategoriesListItemsBinding

class PopularCategoriesAdapter:RecyclerView.Adapter<PopularCategoriesAdapter.PopularCategoriesViewHolder>() {
    inner class PopularCategoriesViewHolder(private val binding: CategoriesListItemsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(category:String){

                binding.tvCategory.text = category
        }
    }


    //to be replaced
    val diffUtilCallBack = object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
          return newItem==oldItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularCategoriesViewHolder {
return PopularCategoriesViewHolder(
    CategoriesListItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
)   }

    override fun onBindViewHolder(holder: PopularCategoriesViewHolder, position: Int) {
        val current_category = differ.currentList[position]
        holder.bind(current_category)
    }

    override fun getItemCount(): Int {
return differ.currentList.size   }


}