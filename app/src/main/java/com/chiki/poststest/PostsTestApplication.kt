package com.chiki.poststest

import android.app.Application
import com.chiki.poststest.room.AppDatabase

class PostsTestApplication: Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}