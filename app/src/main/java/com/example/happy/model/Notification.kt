package com.example.happy.model

import java.io.Serializable

data class Notification(val id: Int, val content: String, val origin: Components, val date: String) : Serializable
