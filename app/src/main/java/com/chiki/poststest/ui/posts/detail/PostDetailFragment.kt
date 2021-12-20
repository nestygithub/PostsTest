package com.chiki.poststest.ui.posts.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.chiki.poststest.PostsTestApplication
import com.chiki.poststest.adapters.CommentAdapter
import com.chiki.poststest.databinding.FragmentPostDetailBinding

class PostDetailFragment : Fragment() {

    //ViewModel
    private lateinit var postDetailViewModel: PostDetailViewModel

    //Binding
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    //Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPostDetailBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedId:Int = PostDetailFragmentArgs.fromBundle(requireArguments()).postId
        val postDetailViewModelFactory = PostDetailViewModelFactory(selectedId,requireActivity().application as PostsTestApplication)
        postDetailViewModel = ViewModelProvider(this,postDetailViewModelFactory).get(PostDetailViewModel::class.java)

        binding.postDetailViewModel = postDetailViewModel
        binding.lifecycleOwner = this

        binding.commentsList.adapter = CommentAdapter{
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}