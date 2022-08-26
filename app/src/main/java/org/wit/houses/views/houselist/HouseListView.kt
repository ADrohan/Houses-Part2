package org.wit.houses.views.houselist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.houses.R
import org.wit.houses.adapters.HouseAdapter
import org.wit.houses.adapters.HouseListener
import org.wit.houses.databinding.ActivityHouseListBinding
import org.wit.houses.main.MainApp
import org.wit.houses.models.HouseModel

class HouseListView : AppCompatActivity(), HouseListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityHouseListBinding
    lateinit var presenter: HouseListPresenter

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
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddHouse()
            }
            R.id.item_map -> { presenter.doShowHousesMap()
            }
            R.id.item_logout -> { presenter.doLogout() }

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onHouseClick(house: HouseModel) {
        presenter.doEditHouse(house)
    }

    override fun onResume() {
        //  binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onResume()
        loadHouses()
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun loadHouses() {
        GlobalScope.launch(Dispatchers.Main){
            binding.recyclerView.adapter =
               HouseAdapter(presenter.getHouses(), this@HouseListView)
        }
        //showHouses(app.houses.findAll())
    }

    fun showHouses (houses: List<HouseModel>) {
        binding.recyclerView.adapter = HouseAdapter(houses, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}
