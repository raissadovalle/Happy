package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.happy.model.Meeting

@Dao
interface MeetingDAO {
    @Query("SELECT * FROM meetings WHERE id = :id")
    fun loadMeetingById(id: String) : Meeting

    @Query("SELECT * FROM meetings WHERE repId = :repId")
    fun loadMeetingsByRepId(repId: String) : LiveData<MutableList<Meeting>>

    @Insert
    fun insert(meeting: Meeting)

    @Update
    fun update(meeting: Meeting)

    @Delete
    fun delete(meeting: Meeting)
}