package org.wit.houses.views.house

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.houses.databinding.ActivityHouseBinding
import org.wit.houses.helpers.checkLocationPermissions
import org.wit.houses.helpers.createDefaultLocationRequest
import org.wit.houses.helpers.showImagePicker
import org.wit.houses.main.MainApp
import org.wit.houses.models.HouseModel
import org.wit.houses.models.Location
import org.wit.houses.views.location.EditLocationView
import org.wit.houses.views.login.LoginView
import timber.log.Timber

class HousePresenter  (private val view: HouseView) {

    var house = HouseModel()
    var app: MainApp = view.application as MainApp
    var map: GoogleMap? = null
    val locationRequest = createDefaultLocationRequest()
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)

    var binding: ActivityHouseBinding = ActivityHouseBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false
    private val location = Location(52.245696, -7.139102, 15f)

    init {
        if (view.intent.hasExtra("house_edit")) {
            edit = true
            house = view.intent.extras?.getParcelable("house_edit")!!
            view.showPlacemark(house)
        }else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            house.location.lat = location.lat
            house.location.lng = location.lng
        }
        registerImagePickerCallback()
        registerMapCallback()
        doPermissionLauncher()
    }

    suspend fun doAddOrSave(address: String, description: String,
                    bedrooms: String, bathrooms: String, soldPrice: String, listPrice: String, listDate: String, soldDate: String) {
        house.address = address
        house.description = description
        house.bedrooms = bedrooms
        house.bathrooms = bathrooms
        house.soldPrice = soldPrice
        house.listPrice = listPrice
        house.listDate = listDate
        house.soldDate = soldDate

        if (edit) {
            app.houses.update(house)
            Timber.i("update Button Pressed: $house")

        } else {
            app.houses.create(house)
        }

        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    suspend fun doDelete() {
        app.houses.delete(house)
        Timber.i("delete Button Pressed: $house")
        view.setResult(Activity.RESULT_OK)
        view.finish()

    }
    fun doLogout(){
        view.finish()
        val launcherIntent = Intent(view, LoginView::class.java)
        mapIntentLauncher.launch(launcherIntent)
    }


    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {
      //  val location = Location(52.245696, -7.139102, 15f)
        if (house.location.zoom != 0f) {
            location.lat =  house.location.lat
            location.lng = house.location.lng
            location.zoom = house.location.zoom
            locationUpdate(house.location.lat, house.location.lng)
        }
            val launcherIntent = Intent(view, EditLocationView::class.java)
               .putExtra("location", location)
           mapIntentLauncher.launch(launcherIntent)
    }
    fun cachePlacemark (address: String, description: String,
                        bedrooms: String, bathrooms: String, soldPrice: String, listPrice: String, listDate: String, soldDate: String ) {
        house.address = address
        house.description = description
        house.bedrooms = bedrooms
        house.bathrooms = bathrooms
        house.soldPrice = soldPrice
        house.listPrice = listPrice
        house.listDate = listDate
        house.soldDate = soldDate
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            house.image = result.data!!.data!!
                            view.updateImage(house.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            house.location.lat = location.lat
                            house.location.lng = location.lng
                            house.location.zoom = location.zoom
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(house.location.lat, house.location.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        house.location.lat = lat
        house.location.lng = lng
        house.location.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(house.address).position(LatLng(house.location.lat, house.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(house.location.lat, house.location.lng), house.location.zoom))
        view.showPlacemark(house)
    }

    private fun doPermissionLauncher() {
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if(locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

}