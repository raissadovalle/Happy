package com.example.happy.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "notifications")
data class Notification(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                        val memberId: String,
                        val content: String,
                        val component: Components,
                        val date: String) : Serializable {

                        enum class Components(val message: String) {
                            BILLS("Contas"),
                            CLEANINGS("Limpeza"),
                            MEMBERS("Membros"),
                            SHOPPINGS("Compras"),
                            MEETINGS("Reuniões"),
                            MYREP("Minha República")
                        }

}
