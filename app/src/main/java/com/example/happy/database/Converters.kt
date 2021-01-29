package com.example.happy.database

import androidx.room.TypeConverter
import com.example.happy.model.*

class Converters {

    @TypeConverter
    fun fromBillType(billType: BillItem.BillType) : String = billType.toString()

    @TypeConverter
    fun toBillType(string: String) : BillItem.BillType = BillItem.BillType.valueOf(string)

    @TypeConverter
    fun fromComponents(components: Notification.Components) : String = components.toString()

    @TypeConverter
    fun toComponents(string: String) : Notification.Components = Notification.Components.valueOf(string)

    @TypeConverter
    fun fromSecretQuestions(secretQuestions: Member.SecretQuestions) : String = secretQuestions.toString()

    @TypeConverter
    fun toSecretQuestions(string: String) : Member.SecretQuestions = Member.SecretQuestions.valueOf(string)

    @TypeConverter
    fun fromFrequency(frequency: Cleaning.Frequency) : String = frequency.toString()

    @TypeConverter
    fun toFrequency(string: String) :Cleaning.Frequency = Cleaning.Frequency.valueOf(string)
}