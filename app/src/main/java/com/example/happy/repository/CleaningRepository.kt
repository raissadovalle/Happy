package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.Cleaning

class CleaningRepository(application: Application) {

    private val cleaningDao = AppDatabase.getDatabase(application).cleaningDao()

    fun loadShoppingsByRepId(repId: String) = cleaningDao.loadCleaningByRep(repId)
}