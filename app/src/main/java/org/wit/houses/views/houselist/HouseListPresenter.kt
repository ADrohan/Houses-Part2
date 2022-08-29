package org.wit.houses.views.houselist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.houses.main.MainApp
import org.wit.houses.models.HouseModel
import org.wit.houses.views.house.HouseView
import org.wit.houses.views.login.LoginView
import org.wit.houses.views.map.HouseMapView

class HouseListPresenter(val view: HouseListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerMapCallback()
        registerRefreshCallback()
    }

    suspend fun getHouses() = app.houses.findAll()

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

     suspend fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        app.houses.clear()
        val launcherIntent = Intent(view, LoginView::class.java)
        mapIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main) {
                    getHouses()
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}