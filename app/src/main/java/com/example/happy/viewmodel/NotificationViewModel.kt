package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.model.Meeting
import com.example.happy.model.Notification
import com.example.happy.repository.NotificationRepository

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val notificationRepository = NotificationRepository(getApplication())

    fun getNotificationByMemberId(memberId: String) = notificationRepository.loadNotificationsByMemberId(memberId)

    fun create(notification: Notification) = notificationRepository.insert(notification)

    fun delete(notification: Notification) = notificationRepository.delete(notification)
}