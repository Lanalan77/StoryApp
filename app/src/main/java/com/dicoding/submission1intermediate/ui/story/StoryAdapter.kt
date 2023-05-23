package com.dicoding.submission1intermediate.ui.story

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submission1intermediate.databinding.StoryItemBinding

class StoryAdapter(private val storyList: ArrayList<StoryDataClass>): RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(var binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val(id, name, photo) = storyList[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.binding.ivItemPhoto)
        holder.binding.tvItemName.text = name

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intentDetail.putExtra("key_story", id)
            holder.itemView.context.startActivity(intentDetail)
        }

    }

    override fun getItemCount() = storyList.size

}