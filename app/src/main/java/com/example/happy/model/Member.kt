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
                  var graduate: String?,
                  var secretQuestion: SecretQuestions,
                  var secretAnswer: String
                  //var image: String?
                  ) : Serializable {
   // @Ignore constructor(): this("", "", "", "", "", "", "", "", "", "")

    enum class SecretQuestions (val message: String) {
        COLOR("Qual sua cor favorita?"),
        PET_NAME("Qual o nome do seu animal de estimação?"),
        FOOD("Qual a sua comida favorita?"),
        MOVIE("Qual o seu filme preferido?"),
        SONG("Qual a sua música favorita?"),
        ACTOR("Qual o nome do seu ator/atriz favorito?"),
        BOOK("Qual o nome do seu livro favorito?"),
        CITY("Qual a sua cidade favorita?"),
        GIRLFRIENDS("Qual o nome da sua primeira namorada?"),
        BOYFRIEND("Qual o nome do seu primeiro namorado?")
    }
}


