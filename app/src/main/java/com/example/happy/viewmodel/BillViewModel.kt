package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.repository.BillRepository

class BillViewModel(application: Application) : AndroidViewModel(application) {

    private val billRepository = BillRepository(getApplication())

    fun getBillByRepId(repId: String) = billRepository.loadBillsByRepId(repId)
}