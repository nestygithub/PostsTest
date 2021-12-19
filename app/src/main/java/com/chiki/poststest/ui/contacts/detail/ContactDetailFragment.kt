package com.chiki.poststest.ui.contacts.detail

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chiki.poststest.PostsTestApplication
import com.chiki.poststest.databinding.FragmentContactDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class ContactDetailFragment : Fragment() {
    //ViewModels
    private lateinit var contactDetailViewModel: ContactDetailViewModel

    //Binding
    private var _binding:FragmentContactDetailBinding? = null
    private val binding get() = _binding!!

    //Used to request WRITE_CONTACT permission to delete
    private val permissionRequestToDelete = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when(granted){
            true->  contactDetailViewModel.permissionGrantedToDelete()
            false-> contactDetailViewModel.permissionDenied()
        }
    }
    //Used to request WRITE_CONTACT permission to update
    private val permissionRequestToUpdate = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when(granted){
            true->  contactDetailViewModel.permissionGrantedToUpdate(
                binding.nameContact.text.toString(),
                binding.numberContact.text.toString()
            )
            false-> contactDetailViewModel.permissionDenied()
        }
    }

    //Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentContactDetailBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedContact = ContactDetailFragmentArgs.fromBundle(requireArguments()).selectedContact                                  //Gets the selected contact from arguments
        val viewModelProvider = ContactDetailViewModelFactory(selectedContact,requireActivity().application as PostsTestApplication)    //Creates the viewModelFactory to create the viewModel later
        contactDetailViewModel = ViewModelProvider(this,viewModelProvider).get(ContactDetailViewModel::class.java)               //Crates the viewModel

        binding.contactDetailViewModel = contactDetailViewModel     //Binding the viewModel
        binding.lifecycleOwner = this

        //Observers
        contactDetailViewModel.currentContactState.observe(viewLifecycleOwner){state->
            state?.let {
                when(state){
                    CurrentContactState.DETAIL ->{ showEditAndDeleteButtons()}
                    CurrentContactState.EDIT ->{showCancelAndSaveButtons()}
                }
            }
        }
        contactDetailViewModel.navigateToContactsFragment.observe(viewLifecycleOwner){navigate->
            if(navigate){
                navigateToContactsFragment()
                contactDetailViewModel.doneNavigateToContactsFragment()
            }
        }
        contactDetailViewModel.showMessageContactDeleted.observe(viewLifecycleOwner){showMessage->
            if(showMessage){
                showMessageContactDeleted()
                contactDetailViewModel.doneShowMessageContactDeleted()
            }
        }
        contactDetailViewModel.showMessageContactUpdated.observe(viewLifecycleOwner){showMessage->
            if (showMessage){
                showMessageContactUpdated()
            }
        }
        contactDetailViewModel.showMessageSureToDelete.observe(viewLifecycleOwner){showMessage->
            if(showMessage){
                showMessageSureToDeleteContact()
                contactDetailViewModel.doneShowMessageSureToDelete()
            }
        }
        contactDetailViewModel.showMessagePermissionDenied.observe(viewLifecycleOwner){showMessage->
            if (showMessage){
                Toast.makeText(requireContext(),"Permission has been denied!",Toast.LENGTH_LONG).show()
                contactDetailViewModel.doneShowMessagePermissionDenied()
            }
        }

        contactDetailViewModel.requestWriteContactPermissionToDelete.observe(viewLifecycleOwner){request->
            if (request){
                askForPermissionToDelete()
            }
        }
        contactDetailViewModel.requestWriteContactPermissionToUpdate.observe(viewLifecycleOwner){request->
            if(request){
                askForPermissionToUpdate()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Actions
    private fun showEditAndDeleteButtons(){
        //Todo Enable disable EditText
        binding.editButton.visibility = View.VISIBLE
        binding.deleteButton.visibility = View.VISIBLE
        binding.cancelButton.visibility = View.GONE
        binding.saveButton.visibility = View.GONE
        binding.nameContact.isEnabled = false
        binding.numberContact.isEnabled = false
    }
    private fun showCancelAndSaveButtons(){
        //Todo Enable disable EditText
        binding.editButton.visibility = View.GONE
        binding.deleteButton.visibility = View.GONE
        binding.cancelButton.visibility = View.VISIBLE
        binding.saveButton.visibility = View.VISIBLE
        binding.nameContact.isEnabled = true
        binding.numberContact.isEnabled = true
    }
    private fun navigateToContactsFragment(){
        val action = ContactDetailFragmentDirections.actionContactDetailFragmentToContactsFragment()
        findNavController().navigate(action)
    }
    private fun showMessageContactDeleted(){
        Snackbar.make(requireActivity().findViewById(android.R.id.content),"Contact Deleted!",Snackbar.LENGTH_LONG).show()
    }
    private fun showMessageContactUpdated(){
        Snackbar.make(requireActivity().findViewById(android.R.id.content),"Contact Updated!",Snackbar.LENGTH_LONG).show()
    }
    private fun showMessageSureToDeleteContact(){
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Do you want to delete this contact?")
            .setNegativeButton("CANCEL") { _, _ -> }
            .setPositiveButton("DELETE") { _, _ -> contactDetailViewModel.deleteContact() }
            .show()
    }

    private fun askForPermissionToDelete() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionRequestToDelete.launch(Manifest.permission.WRITE_CONTACTS)
        } else {
            contactDetailViewModel.permissionGrantedToDelete()
        }
    }
    private fun askForPermissionToUpdate() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionRequestToUpdate.launch(Manifest.permission.WRITE_CONTACTS)
        } else {
            contactDetailViewModel.permissionGrantedToUpdate(
                binding.nameContact.text.toString(),
                binding.numberContact.text.toString()
            )
        }
    }
}