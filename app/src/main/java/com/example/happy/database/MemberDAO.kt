package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.happy.model.Member

@Dao
interface MemberDAO {

    @Query("SELECT * FROM members WHERE id = :memberId")
    fun loadMemberById(memberId: String?) : LiveData<Member>

    @Query("SELECT * FROM members WHERE email = :email AND password =:password")
    fun login(email: String, password: String) : Member

    @Query("SELECT * FROM members WHERE repId = :repId")
    fun loadMembersByRep(repId: String) : LiveData<List<Member>>

    @Insert
    fun insert(member : Member)

    @Update
    fun update(member: Member)
}