package com.example.happy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "members")
data class Member(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                  val repId: String?,
                  val name: String,
                  val celNumber: String?,
                  val email: String,
                  val password: String,
                  val hometown: String?,
                  val hometownState: String?,
                  val college: String?,
                  val graduate: String?) : Serializable
