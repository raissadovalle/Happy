package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.BillItem
import com.example.happy.model.Meeting

class MeetingRepository(application: Application) {

    private val meetingDao = AppDatabase.getDatabase(application).meetingDao()

    fun loadMeetingById(id: String) : Meeting = meetingDao.loadMeetingById(id)

    fun loadMeetingsByRepId(repId: String) = meetingDao.loadMeetingsByRepId(repId)

    fun insert(meeting: Meeting) = meetingDao.insert(meeting)

    fun update(meeting: Meeting) = meetingDao.update(meeting)

    fun delete(meeting: Meeting) = meetingDao.delete(meeting)
}
