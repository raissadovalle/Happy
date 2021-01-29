package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.happy.model.Cleaning

@Dao
interface CleaningDAO {
    @Query("SELECT * FROM cleanings WHERE id = :repId")
    fun loadCleaningByRep(repId: String) : LiveData<MutableList<Cleaning>>

    @Insert
    fun insert(cleaning: Cleaning)

    @Update
    fun update(cleaning: Cleaning)

    @Update
    fun updateLastCleaned(cleaning: Cleaning)

    @Delete
    fun delete(cleaning: Cleaning)
}