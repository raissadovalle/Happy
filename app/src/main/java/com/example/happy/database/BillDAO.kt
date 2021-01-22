package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.happy.model.BillItem

@Dao
interface BillDAO {
    @Query("SELECT * FROM bills WHERE id = :billId")
    fun loadBillById(billId: String): BillItem

    @Query("SELECT * FROM bills WHERE repId = :repId")
    fun loadBillsByRepId(repId: String) : LiveData<List<BillItem>>

    @Insert
    fun insert(bill: BillItem)

    @Update
    fun update(bill: BillItem)
}