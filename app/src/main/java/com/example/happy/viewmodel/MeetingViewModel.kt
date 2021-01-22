package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.repository.MeetingRepository

class MeetingViewModel (application: Application) : AndroidViewModel(application) {

    private val meetingRepository = MeetingRepository(getApplication())

    fun getMeetingsByRepId(repId: String) = meetingRepository.loadMeetingsByRepId(repId)
}