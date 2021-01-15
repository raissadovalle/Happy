package com.example.happy.model

import java.io.Serializable

data class Member(val id: Int, val name: String, val celNumber: String, val email: String, val profilePhoto: ProfilePhoto) : Serializable
