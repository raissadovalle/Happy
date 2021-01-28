package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.model.BillItem
import com.example.happy.model.Member
import com.example.happy.repository.BillRepository

class BillViewModel(application: Application) : AndroidViewModel(application) {

    private val billRepository = BillRepository(getApplication())

    fun getBillByRepId(repId: String) = billRepository.loadBillsByRepId(repId)

    fun createBill(bill: BillItem) = billRepository.insert(bill)

    fun updateBill(bill: BillItem) = billRepository.update(bill)

    fun updateCloseBill(id: String, isClosed: Boolean) = billRepository.updateClosed(id, isClosed)

    fun deleteBill(bill: BillItem) = billRepository.delete(bill)
}