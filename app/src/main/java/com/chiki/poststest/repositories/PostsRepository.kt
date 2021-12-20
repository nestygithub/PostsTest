package com.chiki.poststest.repositories

import androidx.lifecycle.LiveData
import com.chiki.poststest.api.Network
import com.chiki.poststest.models.Comment
import com.chiki.poststest.models.Post
import com.chiki.poststest.models.PostWithComments
import com.chiki.poststest.room.AppDatabase
import java.lang.Exception

class PostsRepository(private val database: AppDatabase) {

    val posts: LiveData<List<Post>> = database.postDao().getAllPosts()

    suspend fun refreshPosts(){
        try {
            val listResult:List<Post> =Network.posts.getPosts()
            if (listResult.isNotEmpty()){
                database.postDao().insertAll(*listResult.toTypedArray())
            }
        }catch (e:Exception){
            throw e
        }
    }

    suspend fun getPostWithComments(postId:Int): PostWithComments {
        return try {
            val listResult:List<Comment> = Network.posts.getComments(postId)
            if (listResult.isNotEmpty()){
                database.commentsDao().insertAll(*listResult.toTypedArray())
            }
            database.postDao().getPostWithComments(postId)
        }catch (e:Exception){
            database.postDao().getPostWithComments(postId)
        }
    }
}