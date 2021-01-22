package com.example.happy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "reps")
data class Rep(@PrimaryKey val id: String = UUID.randomUUID().toString(),
               val name: String,
               val address: String?) : Serializable
