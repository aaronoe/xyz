package de.aaronoe.xyz

import android.app.Application
import android.os.StrictMode
import com.google.firebase.FirebaseApp


class xyzApp : Application() {

    companion object {
        lateinit var instance : xyzApp
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }
}