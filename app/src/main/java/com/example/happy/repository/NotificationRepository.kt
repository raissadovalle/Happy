package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.Meeting
import com.example.happy.model.Member
import com.example.happy.model.Notification

class NotificationRepository(application: Application) {

    private val notificationDao = AppDatabase.getDatabase(application).notificationDao()

    fun loadNotificationsByMemberId(memberId: String) = notificationDao.loadNotificationsByMemberId(memberId)

    fun insert(notification: Notification) = notificationDao.insert(notification)

    fun delete(notification: Notification) = notificationDao.delete(notification)
}