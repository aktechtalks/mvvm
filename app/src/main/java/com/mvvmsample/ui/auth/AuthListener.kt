package com.mvvmsample.ui.auth

import com.mvvmsample.db.entities.User

interface AuthListener {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(message: String)
    fun onActivityStart(activity: Class<*>)
}
