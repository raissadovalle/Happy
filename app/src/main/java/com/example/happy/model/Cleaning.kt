package com.example.happy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "cleanings")
data class Cleaning(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                    val desc: String,
                    var memberId: String?,
                    val repId: String,
                    var lastCleaned: String?,
                    var memberName: String?,
                    var wasCleaned: Boolean,
                    var frequency: Frequency) : Serializable {

                    enum class Frequency(val message: String) {
                        WEEKLY("7 dias"),
                        BIWEEKLY("15 dias"),
                        MONTLY("30 dias")
                    }
}

