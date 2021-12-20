package com.chiki.poststest.models

import androidx.room.Embedded
import androidx.room.Relation

data class PostWithComments(
    @Embedded
    val post: Post,

    @Relation(parentColumn = "id", entityColumn = "post_id")
    val comments: List<Comment> = emptyList()
)