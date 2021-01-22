package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.BillItem

class BillRepository(application: Application) {

    private val billDao = AppDatabase.getDatabase(application).billDao()

    fun loadBillById(id: String) : BillItem = billDao.loadBillById(id)

    fun loadBillsByRepId(repId: String) = billDao.loadBillsByRepId(repId)
}