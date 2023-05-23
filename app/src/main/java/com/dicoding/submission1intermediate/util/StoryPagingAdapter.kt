package com.dicoding.submission1intermediate.util

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submission1intermediate.databinding.StoryItemBinding
import com.dicoding.submission1intermediate.room.UserStoryEntity
import com.dicoding.submission1intermediate.ui.story.StoryDetailActivity

class StoryPagingAdapter :
    PagingDataAdapter<UserStoryEntity, StoryPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intentDetail.putExtra("key_story", data?.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserStoryEntity) {
            Glide.with(binding.root.context)
                .load(data.photoStory)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = data.user

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserStoryEntity>() {
            override fun areItemsTheSame(oldItem: UserStoryEntity, newItem: UserStoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserStoryEntity, newItem: UserStoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}