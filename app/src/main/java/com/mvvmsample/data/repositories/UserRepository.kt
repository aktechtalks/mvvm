package com.mvvmsample.data.repositories

import com.mvvmsample.data.network.MyApi
import com.mvvmsample.data.network.SafeApiRequest
import com.mvvmsample.data.network.responses.AuthResponse
import com.mvvmsample.db.AppDatabase
import com.mvvmsample.db.entities.User

class UserRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun userLogin(email: String, password: String): AuthResponse {
        return apiRequest { api.userLogin(email, password) }
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()

    fun deleteUser() = db.getUserDao().deleteUser()

    suspend fun userSignUp(name: String, email: String, password: String): AuthResponse {
        return apiRequest { api.userSignUp(name, email, password) }
    }
}
