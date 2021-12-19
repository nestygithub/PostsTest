package com.chiki.poststest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chiki.poststest.databinding.ContactItemBinding
import com.chiki.poststest.models.Contact

class ContactAdapter (private val onItemClicked:(Contact)->Unit): ListAdapter<Contact, ContactAdapter.ContactViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder.from(this, parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ContactViewHolder private constructor(private val binding:ContactItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(contact: Contact) {
            binding.contact = contact
            binding.executePendingBindings()
        }
        companion object{
            fun from(sleepNightAdapter: ContactAdapter, parent: ViewGroup, onItemClicked: (Contact) -> Unit): ContactViewHolder {
                val viewHolder = ContactViewHolder(ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
                viewHolder.itemView.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    onItemClicked(sleepNightAdapter.getItem(position))
                }
                return viewHolder
            }
        }
    }

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }
        }
    }
}