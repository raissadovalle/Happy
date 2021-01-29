package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.happy.model.Cleaning

@Dao
interface CleaningDAO {
    @Query("SELECT * FROM cleanings WHERE repId = :repId")
    fun loadCleaningByRep(repId: String) : LiveData<MutableList<Cleaning>>

    @Query("SELECT * FROM cleanings WHERE repId = :repId")
    fun loadCleaningByRepNoLiveData(repId: String) : MutableList<Cleaning>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cleaning: Cleaning)

//    @Query("INSERT INTO cleanings (desc, repId, frequency, wasCleaned) VALUES (:desc, :repId, :frequency, false)")
//    fun insert(desc: String, repId: String, frequency: String)

    @Update
    fun update(cleaning: Cleaning)

    @Update
    fun updateLastCleaned(cleaning: Cleaning)

    @Delete
    fun delete(cleaning: Cleaning)
}