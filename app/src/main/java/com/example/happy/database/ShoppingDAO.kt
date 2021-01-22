package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.happy.model.Shopping

@Dao
interface ShoppingDAO {
    @Query("SELECT * FROM shoppings WHERE repId = :repId")
    fun loadShoppingsByRepId(repId: String) : LiveData<List<Shopping>>

    @Insert
    fun insert(shopping: Shopping)
}