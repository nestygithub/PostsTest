package com.chiki.poststest.ui.posts.detail

import androidx.lifecycle.*
import com.chiki.poststest.PostsTestApplication
import com.chiki.poststest.models.PostWithComments
import com.chiki.poststest.repositories.PostsRepository
import kotlinx.coroutines.launch

class PostDetailViewModel(val selectedPostId:Int, postsRepository: PostsRepository): ViewModel() {

    private val _postWithComments = MutableLiveData<PostWithComments>()
    val postWithComments:LiveData<PostWithComments> get() = _postWithComments

    //Lifecycle
    init {
        viewModelScope.launch {
            _postWithComments.value = postsRepository.getPostWithComments(selectedPostId)
        }
    }
}

class PostDetailViewModelFactory(private val selectedPostId:Int, private val context: PostsTestApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)) {
            val database = context.database
            @Suppress("UNCHECKED_CAST")
            return PostDetailViewModel(selectedPostId,PostsRepository(database)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}