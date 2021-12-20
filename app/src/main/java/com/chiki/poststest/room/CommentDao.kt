package com.chiki.poststest.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chiki.poststest.models.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg newComment: Comment)

    @Update
    suspend fun update(updatedComment: Comment)

    @Delete
    suspend fun delete(oldComment: Comment)

    @Query("DELETE FROM comment")
    suspend fun clear()

    @Query("SELECT * FROM comment")
    fun getAllComments(): LiveData<List<Comment>>
    @Query("SELECT * FROM comment WHERE id = :commentId")
    fun getComment(commentId:Int): Comment

    @Query("SELECT * FROM comment WHERE post_id = :postId")
    suspend fun getCommentOfPost(postId: Int): List<Comment>
}