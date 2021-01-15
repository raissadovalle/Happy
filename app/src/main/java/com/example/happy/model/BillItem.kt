package com.example.happy.model

import java.io.Serializable
import java.util.*

data class BillItem(val id: Int,
                    val Desc: String,
                    val Price: Double,
                    val Type: BillType,
                    val Date: String) : Serializable
