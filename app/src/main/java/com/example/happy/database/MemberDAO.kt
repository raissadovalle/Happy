package com.example.happy.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.happy.model.Member

@Dao
interface MemberDAO {

    @Query("SELECT * FROM members WHERE id = :memberId")
    fun loadMemberById(memberId: String?) : LiveData<Member>

    @Query("SELECT * FROM members WHERE id = :memberId")
    fun loadMemberByIdNoLiveData(memberId: String?) : Member

    @Query("SELECT EXISTS(SELECT * FROM members WHERE email = :email)")
    fun loadMembersByEmail(email: String) : Boolean

    @Query("SELECT * FROM members WHERE email = :email AND password =:password")
    fun login(email: String, password: String) : Member

    @Query("SELECT * FROM members WHERE repId = :repId")
    fun loadMembersByRep(repId: String) : LiveData<List<Member>>

    @Query("SELECT COUNT(*) FROM members WHERE repId = :repId")
    fun countMembersByRep(repId: String?) : Int

    @Insert
    fun insert(member : Member)

    @Update
    fun update(member: Member)

    @Update
    fun enterRep(member: Member)
}