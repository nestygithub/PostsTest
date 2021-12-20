package com.chiki.poststest.ui.posts.list

import androidx.lifecycle.*
import com.chiki.poststest.PostsTestApplication
import com.chiki.poststest.models.Post
import com.chiki.poststest.repositories.PostsRepository
import kotlinx.coroutines.launch

class PostsViewModel(postsRepository: PostsRepository):ViewModel() {

    //States
    private val _error = MutableLiveData<String>()              //In case of error it holds the message to show
    val error:LiveData<String> get() = _error
    val posts:LiveData<List<Post>> = postsRepository.posts      //List of all posts in database

    //Event
    private val _selectedPost = MutableLiveData<Post>()         //When a post gets selected
    val selectedPost:LiveData<Post> get() = _selectedPost

    //Lifecycle
    init {
        viewModelScope.launch {
            try{
                postsRepository.refreshPosts()
            }
            catch (e:Exception){
                _error.value = e.message
            }
        }
    }

    //UserInput
    fun selectingPost(post:Post){
        _selectedPost.value = post
    }

    //Action
    fun doneNavigateToPostDetailFragment(){
        _selectedPost.value = null
    }
}

class PostsViewModelFactory(private val context: PostsTestApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            val database = context.database
            @Suppress("UNCHECKED_CAST")
            return PostsViewModel(PostsRepository(database)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}