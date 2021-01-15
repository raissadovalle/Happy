package com.example.happy.model

import java.io.Serializable

data class Meeting(val id: Int, val date: String, val subject: String, val debate: String, val verdict: String, val forwarding: String) : Serializable
