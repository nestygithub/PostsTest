package com.chiki.poststest.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chiki.poststest.models.Contact
import com.chiki.poststest.adapters.ContactAdapter

@BindingAdapter("setContactName")
fun TextView.setContactName(contact: Contact?){
    contact?.let {
        text = it.name
    }
}

@BindingAdapter("setContactNumber")
fun TextView.setContactNumber(contact: Contact?){
    contact?.let {
        text = it.number
    }
}

@BindingAdapter("setContactsList")
fun RecyclerView.setContactsList(contacts:List<Contact>?) {
    contacts?.let {
        if (it.isNotEmpty()){
            (adapter as ContactAdapter).submitList(it)
        }
    }
}

@BindingAdapter("setVisible")
fun View.setVisible(loading: Boolean?) {
    loading?.let {
        visibility = when(it){
            true-> View.VISIBLE
            false-> View.GONE
        }
    }
}
