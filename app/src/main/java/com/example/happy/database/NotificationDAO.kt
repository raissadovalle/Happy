package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.happy.model.Notification

@Dao
interface NotificationDAO {

    @Query("SELECT * FROM notifications WHERE memberId = :memberId")
    fun loadNotificationsByMemberId(memberId: String) : LiveData<List<Notification>>

    @Insert
    fun insert(notification: Notification)
}