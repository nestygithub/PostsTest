package com.chiki.poststest.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Contact(
    @PrimaryKey
    val id:String,
    val name:String,
    val number:String
): Parcelable