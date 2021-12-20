package com.chiki.poststest.api

import com.chiki.poststest.models.Comment
import com.chiki.poststest.models.Post
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface PostsApiService{
    @GET("posts")
    suspend fun getPosts():List<Post>

    @GET("comments")
    suspend fun getComments(@Query("postId") postId:Int): List<Comment>
}

object Network{
    val posts: PostsApiService = retrofit.create(PostsApiService::class.java)
}