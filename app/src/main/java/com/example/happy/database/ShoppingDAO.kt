package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.happy.model.Meeting
import com.example.happy.model.Shopping

@Dao
interface ShoppingDAO {

    @Query("SELECT * FROM shoppings WHERE id = :id")
    fun loadShoppingById(id: String) : Shopping

    @Query("SELECT * FROM shoppings WHERE repId = :repId")
    fun loadShoppingsByRepId(repId: String) : LiveData<MutableList<Shopping>>

    @Insert
    fun insert(shopping: Shopping)

    @Update
    fun update(shopping: Shopping)

    @Delete
    fun delete(shopping: Shopping)
}