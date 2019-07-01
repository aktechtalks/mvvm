package com.mvvmsample.data.network.responses

import com.mvvmsample.db.entities.User

data class AuthResponse(
    val isSuccessful : Boolean?,
    val message: String?,
    val user: User?
)