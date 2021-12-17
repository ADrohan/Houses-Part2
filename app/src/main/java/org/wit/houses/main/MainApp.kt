package org.wit.houses.main

import android.app.Application
import org.wit.houses.models.HouseJSONStore
import org.wit.houses.models.HouseMemStore
import org.wit.houses.models.HouseModel
import org.wit.houses.models.HouseStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var houses: HouseStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //houses = HouseMemStore()
        houses = HouseJSONStore(applicationContext)
        i("Houses started")
    }
}
