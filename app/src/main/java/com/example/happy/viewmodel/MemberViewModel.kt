package com.example.happy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.happy.model.Member
import com.example.happy.repository.MemberRepository

class MemberViewModel (application: Application) : AndroidViewModel(application) {

    private val memberRepository = MemberRepository(getApplication())

    fun getMemberByRepId(repId: String) = memberRepository.loadMembersByRepId(repId)

    fun createMember(member: Member) = memberRepository.insert(member)

    fun updateMember(member: Member) = memberRepository.update(member)

    fun enterRep(member: Member) = memberRepository.enterRep(member)

    fun login(email: String, password: String) : MutableLiveData<Member> {
        return MutableLiveData(
                memberRepository.login(email, password).also { member ->
                    PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
                        if(member != null)
                            it.edit().putString(MEMBER_ID, member.id).apply()
                    }
                }
        )
    }

    fun logout() = PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
        it.edit().remove(MEMBER_ID).apply()
    }

    fun isLogged(): LiveData<Member> = PreferenceManager.getDefaultSharedPreferences(getApplication()).let {
        return memberRepository.loadMemberById(it.getString(MEMBER_ID, null))
    }

    companion object {
        val MEMBER_ID = "MEMBER_ID"
    }
}