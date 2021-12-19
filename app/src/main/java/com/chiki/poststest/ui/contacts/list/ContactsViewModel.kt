package com.chiki.poststest.ui.contacts.list

import androidx.lifecycle.*
import com.chiki.poststest.PostsTestApplication
import com.chiki.poststest.models.Contact
import com.chiki.poststest.repositories.ContactsDataSource
import com.chiki.poststest.repositories.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContactsViewModel(private val contactsRepository: ContactsRepository):ViewModel() {

    //States
    val contacts = contactsRepository.contacts                                      //List of contacts
    private var _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> get() = _loading                                  //To show the progress bar while fetching contacts

    //Events
    private var _navigateToDetailFragment = MutableLiveData<Contact>()              //To know when to navigate to detail fragment
    val navigateToDetailFragment: LiveData<Contact> get() = _navigateToDetailFragment
    private var _requestReadContactPermission = MutableLiveData<Boolean>()           //Checks if we have permission to read contacts
    val requestReadContactPermission: LiveData<Boolean> get() = _requestReadContactPermission
    private var _showMessagePermissionDenied = MediatorLiveData<Boolean>()                //To show a message that permission was denied
    val showMessagePermissionDenied: LiveData<Boolean> get() = _showMessagePermissionDenied


    //Lifecycle
    init {
        fetchContacts()
        //fetchFakeContacts()
    }

    //Events
    fun onSelectedContact(contact: Contact) {
        _navigateToDetailFragment.value = contact
    }
    fun doneNavigateToContactDetailFragment() {
        _navigateToDetailFragment.value = null
    }
    fun doneShowMessagePermissionDenied() {
        _showMessagePermissionDenied.value = false
    }
    fun permissionGranted() {
        _requestReadContactPermission.value = false
        getContactsFromPhone()
    }
    fun permissionDenied() {
        _requestReadContactPermission.value = false
        _showMessagePermissionDenied.value = true
    }


    //Actions
    fun fetchContacts() {
        _requestReadContactPermission.value = true
    }
    private fun getContactsFromPhone() {
        viewModelScope.launch {
            _loading.value = true
            contactsRepository.fetchContacts()
            _loading.value = false
        }
    }
}

class ContactsViewModelFactory(private val context: PostsTestApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            val source = ContactsDataSource(context.contentResolver)
            val database = context.database
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(ContactsRepository(source, database, Dispatchers.IO)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}