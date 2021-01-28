package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.happy.model.Member
import com.example.happy.model.Rep

@Dao
interface RepDAO{
    @Query("SELECT * FROM reps WHERE id = :id")
    fun loadRepById(id: String) : LiveData<Rep>

    @Query("SELECT EXISTS(SELECT * FROM reps WHERE id = :repId)")
    fun loadRepByIdNoLiveData(repId: String?) : Boolean

    @Query("SELECT * FROM reps WHERE name = :name AND address = :address")
    fun loadRepByNameAndAddress(name: String, address: String) : Rep

    @Insert
    fun insert(rep: Rep)

    @Update
    fun update(rep: Rep)

    @Delete
    fun delete(rep: Rep)
}