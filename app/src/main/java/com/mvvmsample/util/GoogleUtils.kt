package com.mvvmsample.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.common.api.Status

class GoogleUtils(private val activity: AppCompatActivity) : OnConnectionFailedListener {
    private var googleApiClient: GoogleApiClient? = null
    private var googleListener: GoogleListener? = null
    private var googleSignInOptions: GoogleSignInOptions? = null

    init {
        initCredentials()
    }


    fun setGoogleListener(googleListener: GoogleListener) {
        this.googleListener = googleListener
    }


    private fun initCredentials() {
        googleSignInOptions = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleApiClient = GoogleApiClient.Builder(this.activity).enableAutoManage(activity, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, this.googleSignInOptions!!).build()
    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        googleListener!!.onConnectionFailed(connectionResult.errorCode, connectionResult.errorMessage)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                googleListener!!.onLoginSuccess(result.signInAccount)
            } else {
                googleListener!!.onFailed(result.status)
            }
        }
    }


    fun signIn() {
        googleApiClient?.connect()
        activity.startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), RC_SIGN_IN)
    }


    fun signOut() {
        Auth.GoogleSignInApi.signOut(this.googleApiClient)
            .setResultCallback { status -> this@GoogleUtils.googleListener!!.onFailed(status) }
    }


    fun revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(this.googleApiClient)
            .setResultCallback { status -> this@GoogleUtils.googleListener!!.onFailed(status) }
    }


    interface GoogleListener {
        fun onConnectionFailed(i: Int, str: String?)

        fun onFailed(status: Status)

        fun onLoginSuccess(googleSignInAccount: GoogleSignInAccount?)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
