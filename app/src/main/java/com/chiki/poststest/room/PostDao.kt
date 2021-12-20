package com.chiki.poststest.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chiki.poststest.models.Post
import com.chiki.poststest.models.PostWithComments

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg newPosts: Post)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("DELETE FROM post")
    suspend fun clear()

    @Query("SELECT * FROM post")
    fun getAllPosts(): LiveData<List<Post>>
    @Query("SELECT * FROM post WHERE id = :id")
    suspend fun getPost(id:Int): Post

    @Transaction
    @Query("SELECT * FROM post WHERE id = :postId")
    suspend fun getPostWithComments(postId:Int): PostWithComments

    @Transaction
    @Query("SELECT * FROM post")
    fun getAllPostsWithComments():LiveData<List<PostWithComments>>
}