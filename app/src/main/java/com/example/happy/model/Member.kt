package com.example.happy.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "members")
data class Member(@PrimaryKey var id: String = UUID.randomUUID().toString(),
                  var repId: String?,
                  var name: String,
                  var celNumber: String?,
                  val email: String,
                  var password: String,
                  var hometown: String?,
                  var hometownState: String?,
                  var college: String?,
                  var graduate: String?
                  //var image: String?
                  ) : Serializable {
   // @Ignore constructor(): this("", "", "", "", "", "", "", "", "", "")
}


