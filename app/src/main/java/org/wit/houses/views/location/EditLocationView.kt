package org.wit.houses.views.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.wit.houses.R
import org.wit.houses.databinding.ActivityMapsBinding
import org.wit.houses.models.Location

class EditLocationView : AppCompatActivity(),
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var map: GoogleMap
    lateinit var presenter: EditLocationPresenter
    var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_maps)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        presenter = EditLocationPresenter(this)

        location = intent.extras?.getParcelable<Location>("location")!!
        binding.mapView2.onCreate(savedInstanceState)
        binding.mapView2.getMapAsync {
            it.setOnMarkerDragListener(this)
            it.setOnMarkerClickListener(this)
            presenter.initMap(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_maps_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { presenter.doOnBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
        binding.lat.setText("%.6f".format(marker.position.latitude))
        binding.lng.setText("%.6f".format(marker.position.longitude))
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude,marker.position.longitude)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        return false
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView2.onSaveInstanceState(outState)
    }
}