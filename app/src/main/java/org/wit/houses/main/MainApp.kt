package org.wit.houses.main

import android.app.Application
import org.wit.houses.models.*
import org.wit.houses.room.HouseStoreRoom
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var houses: HouseStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //houses = HouseMemStore()
       // houses = HouseJSONStore(applicationContext)
       // houses = HouseStoreRoom(applicationContext)
        houses = HouseFireStore(applicationContext)
        i("Houses started")
    }
}
