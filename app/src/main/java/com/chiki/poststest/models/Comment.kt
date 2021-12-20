package com.chiki.poststest.models

import androidx.room.*

@Entity(foreignKeys = [ForeignKey(entity = Post::class, parentColumns = ["id"], childColumns = ["post_id"])],indices = [Index("post_id")])
data class Comment(
    @PrimaryKey
    val id:Int,
    @ColumnInfo(name = "post_id") val postId:Int,
    val email:String,
    val name:String,
    val body:String
)