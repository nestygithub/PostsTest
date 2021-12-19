package com.chiki.poststest.ui.contacts.detail

import androidx.lifecycle.*
import com.chiki.poststest.PostsTestApplication
import com.chiki.poststest.models.Contact
import com.chiki.poststest.repositories.ContactsDataSource
import com.chiki.poststest.repositories.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class CurrentContactState {
    EDIT,
    DETAIL
}
class ContactDetailViewModel(selectedContact: Contact,private val contactsRepository: ContactsRepository): ViewModel() {

    //States
    private var _currentContact = MutableLiveData<Contact>()                                //Selected Contact
    val  currentContact: LiveData<Contact> get() = _currentContact
    private var _currentContactState = MutableLiveData<CurrentContactState>()               //To know which buttons to show
    val currentContactState:LiveData<CurrentContactState> get() = _currentContactState

    //Events
    private var _navigateToContactsFragment = MutableLiveData<Boolean>()                    //To know when to navigate to Contacts Fragment
    val navigateToContactsFragment:LiveData<Boolean> get() = _navigateToContactsFragment
    private var _showMessageContactDeleted = MutableLiveData<Boolean>()                     //To show the message when the contact has been deleted
    val showMessageContactDeleted:LiveData<Boolean> get() = _showMessageContactDeleted
    private var _showMessageSureToDelete = MutableLiveData<Boolean>()                       //To show the message to be sure to delete contact
    val showMessageSureToDelete:LiveData<Boolean> get() = _showMessageSureToDelete
    private var _showMessageContactUpdated = MutableLiveData<Boolean>()                     //To show the message when the contact was updated
    val showMessageContactUpdated:LiveData<Boolean> get() = _showMessageContactUpdated
    private var _showMessagePermissionDenied = MutableLiveData<Boolean>()                   //To show the message when permission has been denied
    val showMessagePermissionDenied:LiveData<Boolean> get() = _showMessagePermissionDenied
    private var _requestWriteContactPermissionToDelete = MutableLiveData<Boolean>()         //Checks if we have permission to delete contacts
    val requestWriteContactPermissionToDelete: LiveData<Boolean> get() = _requestWriteContactPermissionToDelete
    private var _requestWriteContactPermissionToUpdate = MutableLiveData<Boolean>()         //Checks if we have permission to update contacts
    val requestWriteContactPermissionToUpdate: LiveData<Boolean> get() = _requestWriteContactPermissionToUpdate

    //Lifecycle
    init {
        _currentContact.value = selectedContact
        _currentContactState.value = CurrentContactState.DETAIL
    }

    //User Inputs
    fun onEditButton(){
        _currentContactState.value = CurrentContactState.EDIT
    }
    fun onCancelButton(){
        val contact = currentContact.value
        _currentContact.value = contact
        _currentContactState.value = CurrentContactState.DETAIL
    }
    fun onDeleteButton(){
        _requestWriteContactPermissionToDelete.value = true
    }
    fun onSaveButton(){
        _requestWriteContactPermissionToUpdate.value = true
    }

    //Events
    fun doneNavigateToContactsFragment(){
        _navigateToContactsFragment.value = false
    }
    fun doneShowMessageContactDeleted(){
        _showMessageContactDeleted.value = false
    }
    fun doneShowMessageSureToDelete(){
        _showMessageSureToDelete.value = false
    }
    fun doneShowMessagePermissionDenied(){
        _showMessagePermissionDenied.value = false
    }
    fun permissionGrantedToDelete(){
        _showMessageSureToDelete.value = true
    }
    fun deleteContact(){
        viewModelScope.launch {
            _requestWriteContactPermissionToDelete.value = false
            contactsRepository.deleteContact(currentContact.value!!)
            _showMessageContactDeleted.value = true
            _navigateToContactsFragment.value = true
        }
    }
    fun permissionGrantedToUpdate(contactName:String, contactNumber:String){
        viewModelScope.launch {
            _requestWriteContactPermissionToUpdate.value = false

            val updatedContact = Contact(currentContact.value!!.id,contactName,contactNumber)
            if(contactName!=currentContact.value!!.name){
                contactsRepository.updateContactName(updatedContact)
            }
            if(contactNumber!=currentContact.value!!.number){
                contactsRepository.updateContactNumber(updatedContact)
            }
            _currentContact.value = contactsRepository.fetchContact(updatedContact.id)
            _showMessageContactUpdated.value = true
            _currentContactState.value = CurrentContactState.DETAIL
        }
    }
    fun permissionDenied(){
        _requestWriteContactPermissionToDelete.value = false
        _requestWriteContactPermissionToUpdate.value = false
        _showMessagePermissionDenied.value = true
    }
}

class ContactDetailViewModelFactory(private val selectedContact: Contact, private val context: PostsTestApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactDetailViewModel::class.java)) {
            val source = ContactsDataSource(context.contentResolver)
            val database = context.database
            @Suppress("UNCHECKED_CAST")
            return ContactDetailViewModel(selectedContact, ContactsRepository(source,database,Dispatchers.IO)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}