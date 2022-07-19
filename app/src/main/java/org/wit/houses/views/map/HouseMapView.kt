package org.wit.houses.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.houses.R
import org.wit.houses.databinding.ActivityHouseMapsBinding
import org.wit.houses.databinding.ContentHouseMapsBinding
import org.wit.houses.main.MainApp
import org.wit.houses.models.HouseModel

class HouseMapView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityHouseMapsBinding
    private lateinit var contentBinding: ContentHouseMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: HouseMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp
        binding = ActivityHouseMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = HouseMapPresenter(this)

        contentBinding = ContentHouseMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            GlobalScope.launch(Dispatchers.Main) {
                presenter.doPopulateMap(it)
            }
        }
    }

    fun showHouse(house: HouseModel) {
        contentBinding.includeCard2.houseAddress.text = house!!.address
        contentBinding.includeCard2.listPrice.text = house.listPrice
        contentBinding.includeCard2.soldPrice.text = house.soldPrice
        contentBinding.includeCard2.description.text = house.description
        contentBinding.includeCard2.bathrooms.text = house.bathrooms
        contentBinding.includeCard2.bedrooms.text = house.bedrooms
        contentBinding.includeCard2.listDate.text = house.listDate
        contentBinding.includeCard2.soldDate.text = house.soldDate
        contentBinding.includeCard2.lat.text = house.location.lat.toString()
        contentBinding.includeCard2.lng.text = house.location.lng.toString()
        Picasso.get()
            .load(house.image)
            .placeholder(R.drawable.orange_house)
            .resize(200,200)
            .into(contentBinding.includeCard2.imageIcon)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        GlobalScope.launch(Dispatchers.Main) {
            presenter.doMarkerSelected(marker)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}

