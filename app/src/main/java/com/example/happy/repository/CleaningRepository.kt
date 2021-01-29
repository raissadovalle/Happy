package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.BillItem
import com.example.happy.model.Cleaning

class CleaningRepository(application: Application) {

    private val cleaningDao = AppDatabase.getDatabase(application).cleaningDao()

    fun loadCleaningByRep(repId: String) = cleaningDao.loadCleaningByRep(repId)

    fun loadCleaningByRepNoLiveData(repId: String) = cleaningDao.loadCleaningByRepNoLiveData(repId)

    fun updateLastCleaning(cleaning: Cleaning) = cleaningDao.updateLastCleaned(cleaning)

    fun insert(cleaning: Cleaning) = cleaningDao.insert(cleaning)

   //fun insert(cleaning: Cleaning) = cleaningDao.insert(cleaning.desc, cleaning.repId, cleaning.frequency.name)

    fun update(cleaning: Cleaning) = cleaningDao.update(cleaning)

    fun delete(cleaning: Cleaning) = cleaningDao.delete(cleaning)
}