package org.wit.geosite.main

import android.app.Application
import org.wit.geosite.models.*
import org.wit.geosite.room.GeositeStoreRoom
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var geosites: GeositeStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        geosites = GeositeFireStore(applicationContext)

        i("Geosite started")

    }
}