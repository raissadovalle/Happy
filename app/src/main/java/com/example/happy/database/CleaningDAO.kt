package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.happy.model.Cleaning

@Dao
interface CleaningDAO {
    @Query("SELECT * FROM cleanings WHERE id = :repId")
    fun loadCleaningByRep(repId: String) : LiveData<List<Cleaning>>

    @Insert
    fun insert(cleaning: Cleaning)
}