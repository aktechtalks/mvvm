package com.mvvmsample

import android.app.Application
import com.mvvmsample.data.network.MyApi
import com.mvvmsample.data.network.NetworkConnectionInterceptor
import com.mvvmsample.data.repositories.UserRepository
import com.mvvmsample.db.AppDatabase
import com.mvvmsample.ui.auth.AuthViewModelFactory
import com.mvvmsample.util.CustomProgressBar
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MVVMApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from singleton { AuthViewModelFactory(instance()) }
    }

}