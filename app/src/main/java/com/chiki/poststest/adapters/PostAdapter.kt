package com.chiki.poststest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chiki.poststest.databinding.PostItemListBinding
import com.chiki.poststest.models.Post

class PostAdapter(private val onItemClicked:(Post)->Unit): ListAdapter<Post, PostAdapter.PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(this, parent, onItemClicked)
    }
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder private constructor(private val binding:PostItemListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post) {
            binding.post = post
            binding.executePendingBindings()
        }
        companion object{
            fun from(postAdapter: PostAdapter, parent: ViewGroup, onItemClicked: (Post) -> Unit): PostViewHolder {
                val viewHolder = PostViewHolder(PostItemListBinding.inflate(LayoutInflater.from(parent.context), parent,false))
                viewHolder.itemView.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    onItemClicked(postAdapter.getItem(position))
                }
                return viewHolder
            }
        }
    }

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}
