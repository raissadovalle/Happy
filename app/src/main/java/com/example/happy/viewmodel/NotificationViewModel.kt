package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.happy.repository.NotificationRepository

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val notificationRepository = NotificationRepository(getApplication())

    fun getNotificationByMemberId(memberId: String) = notificationRepository.loadNotificationsByMemberId(memberId)
}