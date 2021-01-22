package com.example.happy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profilePhotos")
data class ProfilePhoto(@PrimaryKey val id: Int, val path: String) {
}