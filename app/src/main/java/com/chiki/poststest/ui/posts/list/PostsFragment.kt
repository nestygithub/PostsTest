package com.chiki.poststest.ui.posts.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chiki.poststest.PostsTestApplication
import com.chiki.poststest.adapters.PostAdapter
import com.chiki.poststest.databinding.FragmentPostsBinding
import com.chiki.poststest.models.Post

class PostsFragment : Fragment() {
    //ViewModels
    private val postsViewModel: PostsViewModel by activityViewModels{
        PostsViewModelFactory(requireActivity().application as PostsTestApplication)
    }

    //Binding
    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    //Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPostsBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postsViewModel = postsViewModel
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = PostAdapter{
            postsViewModel.selectingPost(it)
        }

        //Observers
        postsViewModel.error.observe(viewLifecycleOwner){
            it?.let {
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
            }
        }
        postsViewModel.selectedPost.observe(viewLifecycleOwner){selectedPost->
            selectedPost?.let {
                navigateToPostDetailFragment(it)
                postsViewModel.doneNavigateToPostDetailFragment()
            }
        }
    }

    private fun navigateToPostDetailFragment(selectedPost: Post) {
        val action = PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(selectedPost.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}