package com.chiki.poststest.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chiki.poststest.models.Contact

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg newContacts:Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("DELETE FROM contact")
    suspend fun clear()

    @Query("SELECT * FROM contact")
    fun getAllContacts(): LiveData<List<Contact>>
    @Query("SELECT * FROM contact WHERE id = :id")
    suspend fun getPost(id:Int): Contact
}