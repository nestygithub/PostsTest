package com.chiki.poststest.ui.contacts.list

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chiki.poststest.PostsTestApplication
import com.chiki.poststest.adapters.ContactAdapter
import com.chiki.poststest.databinding.FragmentContactsBinding
import com.chiki.poststest.models.Contact

class ContactsFragment : Fragment() {
    //ViewModels
    private val contactsViewModel: ContactsViewModel by activityViewModels{
        ContactsViewModelFactory(requireActivity().application as PostsTestApplication)
    }

    //Binding
    private var _binding : FragmentContactsBinding? = null
    private val binding get() = _binding!!

    //Used to request READ_CONTACT permission
    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when(granted){
            true->  contactsViewModel.permissionGranted()
            false-> contactsViewModel.permissionDenied()
        }
    }

    //Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentContactsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        contactsViewModel.fetchContacts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contactsViewModel = contactsViewModel
        binding.lifecycleOwner = this

        binding.recyclerViewContacts.adapter = ContactAdapter{
            contactsViewModel.onSelectedContact(it)
        }

        //Observers
        contactsViewModel.navigateToDetailFragment.observe(viewLifecycleOwner){selectedContact->
            selectedContact?.let {
                navigateToContactDetailFragment(it)
                contactsViewModel.doneNavigateToContactDetailFragment()
            }
        }      //To navigate to Detail Fragment
        contactsViewModel.requestReadContactPermission.observe(viewLifecycleOwner){askForPermission->
            if (askForPermission){
                askForReadPermission()
            }
        }  //To check if we have permission to read contacts
        contactsViewModel.showMessagePermissionDenied.observe(viewLifecycleOwner){showMessage->
            if (showMessage){
                Toast.makeText(requireContext(),"We need your permission to show the contacts.",Toast.LENGTH_LONG).show()
                contactsViewModel.doneShowMessagePermissionDenied()
            }
        }   //To show a message when permission to read contacts is denied
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Actions
    private fun navigateToContactDetailFragment(selectedContact: Contact){
        val action = ContactsFragmentDirections.actionContactsFragmentToContactDetailFragment(selectedContact)
        findNavController().navigate(action)
    }

    private fun askForReadPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionRequest.launch(Manifest.permission.READ_CONTACTS)
        } else {
            contactsViewModel.permissionGranted()
        }
    }
}