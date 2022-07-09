package org.wit.houses.views.houselist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.houses.R
import org.wit.houses.activities.HouseMapsActivity
import org.wit.houses.adapters.HouseAdapter
import org.wit.houses.adapters.HouseListener
import org.wit.houses.databinding.ActivityHouseListBinding
import org.wit.houses.main.MainApp
import org.wit.houses.models.HouseModel
import org.wit.houses.views.house.HouseView

class HouseListView : AppCompatActivity(), HouseListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityHouseListBinding
    lateinit var presenter: HouseListPresenter
    //private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    //private lateinit var mapsIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        presenter = HouseListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadHouses()

        //registerRefreshCallback()
        //registerMapsCallback()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddHouse()
                //val launcherIntent = Intent(this, HouseView::class.java)
                //refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> { presenter.doShowHousesMap()
                // val launcherIntent = Intent(this, HouseMapsActivity::class.java)
                // refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onHouseClick(house: HouseModel) {
        //  val launcherIntent = Intent(this, HouseView::class.java)
        // launcherIntent.putExtra("house_edit", house)
        // mapsIntentLauncher.launch(launcherIntent)
        presenter.doEditHouse(house)
    }

    override fun onResume() {
        //  binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onResume()
        loadHouses()
    }

    // private fun registerRefreshCallback() {
    //     refreshIntentLauncher =
    //         registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    //         { loadHouses() }
    // }

    //private fun registerMapsCallback() {
    //   mapsIntentLauncher =
    //     registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    //   {}
    // }
    private fun loadHouses() {
        showHouses(app.houses.findAll())
    }

    fun showHouses (houses: List<HouseModel>) {
        binding.recyclerView.adapter = HouseAdapter(houses, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}
