package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.happy.model.Member
import com.example.happy.model.Rep
import com.example.happy.repository.RepRepository

class RepViewModel (application: Application) : AndroidViewModel(application) {

    private val repRepository = RepRepository(getApplication())

    fun getRepById(repId: String) = repRepository.loadRepById(repId)

    fun create(rep: Rep) = repRepository.insert(rep)

    fun update(rep: Rep) = repRepository.update(rep)

    fun saveRepId(name: String, address: String) : MutableLiveData<Rep> {
        return MutableLiveData(
            repRepository.loadRepByNameAndAddress(name, address).also { rep ->
                PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
                    if(rep.id.isNotEmpty())
                        it.edit().putString(RepViewModel.REP_ID, rep.id).apply()
                }
            }
        )
    }

    fun removeRepId() = PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
        it.edit().remove(RepViewModel.REP_ID).apply()
    }

    companion object {
        val REP_ID = "REP_ID"
    }
}