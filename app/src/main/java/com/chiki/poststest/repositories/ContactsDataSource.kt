package com.chiki.poststest.repositories

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.chiki.poststest.models.Contact

class ContactsDataSource(private val contentResolver: ContentResolver)  {
    private val DATA_COLS = arrayOf(
        ContactsContract.Data.MIMETYPE,
        ContactsContract.CommonDataKinds.Phone.NUMBER,//phone number
        ContactsContract.Data.CONTACT_ID
    )

    fun fetchContacts(): List<Contact> {
        val result: MutableList<Contact> = mutableListOf()

        val uri: Uri = ContactsContract.Contacts.CONTENT_URI
        val sort: String = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        val cursor = contentResolver.query(uri,arrayOf(ContactsContract.Contacts._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),null,null,sort)
        cursor?.let {
            if (cursor.count >= 0) {
                while (cursor.moveToNext()) {
                    var id = ""
                    var name = ""
                    var number = ""

                    val columnId = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    if (columnId >= 0) id = cursor.getString(columnId)

                    val columnName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    if (columnName >= 0) name = cursor.getString(columnName) ?: ""

                    val uriPhone: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val selection: String = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                    val cursorNumber = contentResolver.query(uriPhone, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER), selection, arrayOf(id),null)
                    cursorNumber?.let {
                        if (cursorNumber.moveToNext()) {
                            val columnNumber = cursorNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            if (columnNumber >= 0) number = cursorNumber.getString(columnNumber) ?: ""
                        }
                    }
                    cursorNumber?.close()
                    result.add(Contact(id, name, number)) //add the item
                }
            }
        }
        cursor?.close()
        return result
    }
    fun fetchContact(contactId: String): Contact {
        val id: String = contactId
        var name = "Unknown name"
        var number = "Unknown number"

        //To get the contact name
        var uri: Uri = ContactsContract.Contacts.CONTENT_URI
        var selection = "${ContactsContract.Contacts._ID} =?"
        val cursor = contentResolver.query(uri,arrayOf(ContactsContract.Contacts.DISPLAY_NAME),selection,arrayOf(id),null)
        cursor?.let {
            if (cursor.count >= 0 && cursor.moveToNext()) {
                val columnName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                if (columnName >= 0) {
                    name = cursor.getString(columnName) ?: ""
                }
            }
        }
        cursor?.close()

        //To get the contact number
        uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        selection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} =?"
        val cursor2 = contentResolver.query(uri,arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),selection,arrayOf(id),null)
        cursor2?.let {
            if (cursor2.count >= 0 && cursor2.moveToNext()) {
                val columnNumber = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (columnNumber >= 0) {
                    number = cursor2.getString(columnNumber) ?: ""
                }
            }
        }
        cursor2?.close()

        //Return the Contact
        return Contact(id, name, number)
    }
    fun updateContactName(contactId: String, contactName: String): Boolean {
        val where: String = String.format(
            "%s = '%s' AND %s = ?",
            DATA_COLS[0]/*mimetype*/,
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
            DATA_COLS[2]/*contactId*/
        )
        val args = arrayOf(contactId)
        val operations: ArrayList<ContentProviderOperation> = arrayListOf()
        operations.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(where, args)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName)
                .build()
        )
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
    fun updateContactNumber(contactId: String, contactNumber: String): Boolean {
        val where = String.format(
            "%s = '%s' AND %s = ?",
            DATA_COLS[0]/*mimetype*/,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
            DATA_COLS[2]/*number*/
        )
        val args = arrayOf(contactId)
        val operations: ArrayList<ContentProviderOperation> = arrayListOf()
        operations.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(where, args)
                .withValue(DATA_COLS[1], contactNumber)
                .build()
        )
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
    fun deleteContact(contactId: String): Boolean {
        val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId)
        val deleted = contentResolver.delete(uri, null, null)
        return deleted > 0
    }
}