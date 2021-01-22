package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.Member

class MemberRepository(application: Application) {

    private val memberDao = AppDatabase.getDatabase(application).memberDao()

    fun loadMemberById(id: String?) = memberDao.loadMemberById(id)

    fun loadMembersByRepId(repId: String) = memberDao.loadMembersByRep(repId)

    fun login(email: String, password:String) = memberDao.login(email, password)

    fun insert(member: Member) = memberDao.insert(member)

}