package com.chiki.poststest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chiki.poststest.databinding.CommentItemBinding
import com.chiki.poststest.models.Comment

class CommentAdapter(private val onItemClicked:(Comment)->Unit) : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(this, parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentViewHolder private constructor(private val binding:CommentItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(comment: Comment) {
            binding.comment = comment
            binding.executePendingBindings()
        }
        companion object{
            fun from(commentAdapter: CommentAdapter, parent: ViewGroup, onItemClicked: (Comment) -> Unit): CommentViewHolder {
                val viewHolder = CommentViewHolder(CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
                viewHolder.itemView.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    onItemClicked(commentAdapter.getItem(position))
                }
                return viewHolder
            }
        }
    }

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
}


//class SleepNightAdapter(private val onItemClicked:(SleepNight)->Unit): ListAdapter<SleepNight, SleepNightAdapter.SleepNightViewHolder>(DiffCallback){
//
//
//
//
//
//
//}