package com.mvvmsample.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.mvvmsample.data.repositories.UserRepository
import com.mvvmsample.util.ApiException
import com.mvvmsample.util.Coroutines
import com.mvvmsample.util.NoInternetException

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var confirmPassword: String? = null

    var authListener: AuthListener? = null

    fun getLoggedInUser() = repository.getUser()

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Email and password can't be blank")
            return
        }

        Coroutines.main {
            try {
                val authResponse = repository.userLogin(email!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
    }

    fun onSignUpButtonClick(view: View) {
        authListener?.onStarted()
        when {
            name.isNullOrEmpty() -> authListener?.onFailure("Enter valid name")
            email.isNullOrEmpty() -> authListener?.onFailure("Enter valid email")
            password.isNullOrEmpty() -> authListener?.onFailure("Password is required")
            !password.equals(confirmPassword) -> authListener?.onFailure("Password not match")

            else -> Coroutines.main {
                try {
                    val authResponse = repository.userSignUp(name!!, email!!, password!!)
                    authResponse.user?.let {
                        authListener?.onSuccess(it)
                        authListener?.onActivityStart(LoginActivity::class.java)
                        return@main
                    }
                    authListener?.onFailure(authResponse.message!!)
                } catch (e: ApiException) {
                    authListener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    authListener?.onFailure(e.message!!)
                }
            }
        }
    }

    fun onLoginTextViewClick(view: View) {
        authListener?.onActivityStart(LoginActivity::class.java)
    }

    fun onSignUpTextViewClick(view: View) {
        authListener?.onActivityStart(SignUpActivity::class.java)
    }

}