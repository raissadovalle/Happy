package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.happy.model.BillItem

@Dao
interface BillDAO {
    @Query("SELECT * FROM bills WHERE id = :billId")
    fun loadBillById(billId: String): BillItem

    @Query("SELECT * FROM bills WHERE repId = :repId")
    fun loadBillsByRepId(repId: String) : LiveData<MutableList<BillItem>>

    @Query("SELECT * FROM bills WHERE repId = :repId AND isClosed != :isClosed")
    fun loadBillsByRepIdAndIsClosed(repId: String, isClosed: Boolean) : MutableList<BillItem>

    @Insert
    fun insert(bill: BillItem)

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    fun update(bill: BillItem)

    @Query("UPDATE bills SET  desc= :desc, price = :price, type = :type WHERE id = :id")
    fun update(desc: String, price: Double, type: BillItem.BillType, id: String)

    @Query("UPDATE bills SET isClosed = :isClosed WHERE id = :id")
    fun updateClosed(id: String, isClosed: Boolean)

    @Delete
    fun delete(bill: BillItem)
}