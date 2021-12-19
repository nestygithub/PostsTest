package com.chiki.poststest.repositories

import androidx.lifecycle.LiveData
import com.chiki.poststest.models.Contact
import com.chiki.poststest.room.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ContactsRepository(private val source: ContactsDataSource, private val database:AppDatabase, private val myDispatcher: CoroutineDispatcher)  {

    val contacts:LiveData<List<Contact>> = database.contactDao().getAllContacts()

    suspend fun fetchContacts(){
        withContext(myDispatcher) {
            val contacts = source.fetchContacts()
            database.contactDao().clear()
            database.contactDao().insertAll(*contacts.toTypedArray())
        }
    }

    suspend fun fetchContact(contactId: String): Contact {
        return withContext(myDispatcher) {
            source.fetchContact(contactId)
        }
    }

    suspend fun updateContactName(contactUpdated:Contact){
        return withContext(myDispatcher) {
            source.updateContactName(contactUpdated.id, contactUpdated.name)
            database.contactDao().update(contactUpdated)
        }
    }

    suspend fun updateContactNumber(contactUpdated:Contact){
        return withContext(myDispatcher) {
            source.updateContactNumber(contactUpdated.id, contactUpdated.number)
            database.contactDao().update(contactUpdated)
        }
    }

    suspend fun deleteContact(contact:Contact){
        return withContext(myDispatcher) {
            source.deleteContact(contact.id)
            database.contactDao().delete(contact)
        }
    }
}