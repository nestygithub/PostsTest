package com.chiki.poststest.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chiki.poststest.models.Comment
import com.chiki.poststest.models.Contact
import com.chiki.poststest.models.Post

@Database(entities = [Contact::class, Post::class,Comment::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactDao
    abstract fun postDao(): PostDao
    abstract fun commentsDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}