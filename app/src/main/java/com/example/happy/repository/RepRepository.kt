package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.Member
import com.example.happy.model.Rep

class RepRepository (application: Application) {

    private val repDao = AppDatabase.getDatabase(application).repDao()

    fun loadRepById(id: String) = repDao.loadRepById(id)

    fun loadRepByIdNoLiveData(id: String?) = repDao.loadRepByIdNoLiveData(id)

    fun loadRepByNameAndAddress(name: String, address: String) = repDao.loadRepByNameAndAddress(name, address)

    fun insert(rep: Rep) = repDao.insert(rep)

    fun update(rep: Rep) = repDao.update(rep)
}