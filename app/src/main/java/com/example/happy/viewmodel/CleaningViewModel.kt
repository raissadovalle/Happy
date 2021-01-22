package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.repository.CleaningRepository

class CleaningViewModel(application: Application) : AndroidViewModel(application) {

    private val cleaningRepository = CleaningRepository(getApplication())

    fun getCleaningByRepId(repId: String) = cleaningRepository.loadShoppingsByRepId(repId)
}