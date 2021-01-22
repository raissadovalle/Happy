package com.example.happy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "shoppings")
data class Shopping(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                    val repId: String,
                    val desc: String,
                    val date: String,
                    val price: Double?) : Serializable
