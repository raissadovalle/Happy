package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.BillItem

class BillRepository(application: Application) {

    private val billDao = AppDatabase.getDatabase(application).billDao()

    fun loadBillById(id: String) : BillItem = billDao.loadBillById(id)

    fun loadBillsByRepId(repId: String) = billDao.loadBillsByRepId(repId)

    fun loadBillsByRepIdAndIsClosed(repId: String, isClosed: Boolean) = billDao.loadBillsByRepIdAndIsClosed(repId, isClosed)

    fun updateClosed(repId: String, isClosed: Boolean) = billDao.updateClosed(repId, isClosed)

    fun insert(bill: BillItem) = billDao.insert(bill)

    fun update(bill: BillItem) = billDao.update(bill.desc, bill.price, bill.type, bill.id)

    fun delete(bill: BillItem) = billDao.delete(bill)
}