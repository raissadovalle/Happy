package com.example.happy.model

import java.io.Serializable

data class Cleaning(val id: Int, val desc: String, val member: Member, val lastCleaned: String, val frequency: Int) : Serializable
