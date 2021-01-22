package com.example.happy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "meetings")
data class Meeting(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                   val repId : String,
                   val date: String,
                   val subject: String,
                   val debate: String?,
                   val verdict: String?,
                   val forwarding: String?) : Serializable
