package org.wit.houses.views.houselist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
//import org.wit.houses.activities.HouseMapView
import org.wit.houses.main.MainApp
import org.wit.houses.models.HouseModel
import org.wit.houses.views.house.HouseView
import org.wit.houses.views.map.HouseMapView

class HouseListPresenter(val view: HouseListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getHouses() = app.houses.findAll()

    fun doAddHouse() {
        val launcherIntent = Intent(view, HouseView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditHouse(house: HouseModel) {
        val launcherIntent = Intent(view, HouseView::class.java)
        launcherIntent.putExtra("house_edit", house)
        // refreshIntentLauncher.launch(launcherIntent)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doShowHousesMap() {
        val launcherIntent = Intent(view, HouseMapView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { getHouses() }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}