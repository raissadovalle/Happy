package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase

class RepRepository (application: Application) {

    private val repDao = AppDatabase.getDatabase(application).repDao()

    fun loadRepById(id: String) = repDao.loadRepById(id)
}