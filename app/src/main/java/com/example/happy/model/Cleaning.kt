package com.example.happy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "cleanings")
data class Cleaning(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                    val desc: String,
                    val memberId: String?,
                    val repId: String,
                    val lastCleaned: String?,
                    val frequency: Frequency) : Serializable {

                    enum class Frequency(val message: String) {
                        BIWEEKLY("15 dias"),
                        WEEKLY("7 dias"),
                        MONTLY("30 dias")
                    }
}

