package org.wit.houses.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
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
            presenter.doPopulateMap(it)
        }
    }

    fun showHouse(house: HouseModel) {
        contentBinding.includeCard.houseAddress.text = house!!.address
        contentBinding.includeCard.listPrice.text = house.listPrice
        contentBinding.includeCard.soldPrice.text = house.soldPrice
        contentBinding.includeCard.description.text = house.description
        contentBinding.includeCard.bathrooms.text = house.bathrooms
        contentBinding.includeCard.bedrooms.text = house.bedrooms
        Picasso.get()
            .load(house.image)
            .placeholder(R.drawable.orange_house)
            .resize(200,200)
            .into(contentBinding.includeCard.imageIcon)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
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

