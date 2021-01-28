package com.example.happy.repository

import android.app.Application
import com.example.happy.database.AppDatabase
import com.example.happy.model.Member


class MemberRepository(application: Application) {

    private val memberDao = AppDatabase.getDatabase(application).memberDao()

    fun loadMemberById(id: String?) = memberDao.loadMemberById(id)

    fun loadMemberByIdNoLiveData(id: String?) = memberDao.loadMemberByIdNoLiveData(id)

    fun countMembersByRep(id: String?) = memberDao.countMembersByRep(id)

    fun loadMembersByRepId(repId: String) = memberDao.loadMembersByRep(repId)

    fun loadMembersByEmail(email: String) = memberDao.loadMembersByEmail(email)

    fun login(email: String, password:String) = memberDao.login(email, password)

    fun insert(member: Member) = memberDao.insert(member)

    fun update(member: Member) = memberDao.update(member)

    fun enterRep(member: Member) = memberDao.enterRep(member)
}