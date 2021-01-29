package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.model.BillItem
import com.example.happy.model.Cleaning
import com.example.happy.repository.CleaningRepository

class CleaningViewModel(application: Application) : AndroidViewModel(application) {

    private val cleaningRepository = CleaningRepository(getApplication())

    fun getCleaningByRepId(repId: String) = cleaningRepository.loadCleaningByRep(repId)

    fun createCleaning(cleaning: Cleaning) = cleaningRepository.insert(cleaning)

    fun updateCleaning(cleaning: Cleaning) = cleaningRepository.update(cleaning)

    fun updateLastCleaning(cleaning: Cleaning) = cleaningRepository.updateLastCleaning(cleaning)

    fun deleteCleaning(cleaning: Cleaning) = cleaningRepository.delete(cleaning)
}