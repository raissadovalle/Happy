package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.model.BillItem
import com.example.happy.model.Meeting
import com.example.happy.repository.MeetingRepository

class MeetingViewModel (application: Application) : AndroidViewModel(application) {

    private val meetingRepository = MeetingRepository(getApplication())

    fun getMeetingsByRepId(repId: String) = meetingRepository.loadMeetingsByRepId(repId)

    fun create(meeting: Meeting) = meetingRepository.insert(meeting)

    fun update(meeting: Meeting) = meetingRepository.update(meeting)

    fun delete(meeting: Meeting) = meetingRepository.delete(meeting)
}