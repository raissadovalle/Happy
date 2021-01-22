package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.happy.model.Rep

@Dao
interface RepDAO{
    @Query("SELECT * FROM reps WHERE id = :id")
    fun loadRepById(id: String) : LiveData<Rep>

    @Insert
    fun insert(rep: Rep)
}