package com.example.happy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "bills")
data class BillItem(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                    val repId: String,
                    val desc: String,
                    val price: Double,
                    val type: BillType,
                    val date: String,
                    var isClosed: Boolean) : Serializable {

                    enum class BillType(val message: String) {
                        RENT("Aluguel"),
                        ELECTRICITY("Luz"),
                        WATER("Água"),
                        SHOPPING("Compras"),
                        OTHERS("Outros")
                    }
}
