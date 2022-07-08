package org.wit.houses.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.houses.R
import org.wit.houses.databinding.ActivityHouseBinding
import org.wit.houses.helpers.showImagePicker
import org.wit.houses.main.MainApp
import org.wit.houses.models.HouseModel
import org.wit.houses.models.Location
import splitties.alertdialog.*
import timber.log.Timber.i
import java.text.SimpleDateFormat
import java.util.*

class HouseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHouseBinding
    var house = HouseModel()

    var edit = false

    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("House Activity started...")

        if (intent.hasExtra("house_edit")) {
            edit = true
            house = intent.extras?.getParcelable("house_edit")!!
            binding.houseAddress.setText(house.address)
            binding.listPrice.setText(house.listPrice.toString())
            binding.bedrooms.setText(house.bedrooms.toString())
            binding.bathrooms.setText(house.bathrooms.toString())
            binding.description.setText(house.description)
            binding.soldPrice.setText(house.soldPrice.toString())
            binding.btnAdd.setText(R.string.Update_house)
            Picasso.get()
                .load(house.image)
                .into(binding.houseImage)
            if (house.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_houseImage)
            }
        }

        binding.btnDelete.setOnClickListener() {
            doIrreversibleStuffOrCancel()
        }

        binding.btnAdd.setOnClickListener() {
            house.address = binding.houseAddress.text.toString()
            house.description = binding.description.text.toString()
         //   house.auctioneer = binding.auctioneer.text.toString()
            house.bedrooms = binding.bedrooms.text.toString().toIntOrNull() ?: 0
            house.bathrooms = binding.bathrooms.text.toString().toIntOrNull() ?: 0
            house.soldPrice = binding.soldPrice.text.toString().toIntOrNull() ?: 0
            house.listPrice = binding.listPrice.text.toString().toIntOrNull() ?: 0

            if (house.address.isEmpty() ) {
                Snackbar.make(it, R.string.enter_houseAddress, Snackbar.LENGTH_LONG)
                    .show()
            }

            else if (house.listPrice == 0 ) {
                Snackbar.make(it,R.string.enter_listPrice, Snackbar.LENGTH_LONG )
                    .show()
            }

            else {
                if (edit) {
                    if (house.soldPrice != 0) {
                        val simpleDateFormat = SimpleDateFormat("dd.MM.yyy")
                        val currentDate: String = simpleDateFormat.format(Date())
                        house.soldDate = currentDate
                        app.houses.update(house.copy())
                        setResult(RESULT_OK)
                        finish()
                    }
                    else {
                        // if (house.soldDate == "") {
                        app.houses.update(house.copy())
                        setResult(RESULT_OK)
                        finish()
                    }

                } else {
                    val simpleDateFormat = SimpleDateFormat("dd.MM.yyy")
                    val currentDate: String = simpleDateFormat.format(Date())
                    house.listDate = currentDate
                    app.houses.create(house.copy())
                    setResult(RESULT_OK)
                    finish()
                }
            }
            i("add Button Pressed: $house")
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        binding.placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (house.zoom != 0f) {
                location.lat =  house.lat
                location.lng = house.lng
                location.zoom = house.zoom
            }
            val launcherIntent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_house, menu)
        if (edit && menu != null) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_save -> {
                app.houses.update(house.copy())
                setResult(RESULT_OK)
                finish()
            }
            R.id.item_delete -> {
                app.houses.delete(house)
                doIrreversibleStuffOrCancel()
                setResult(RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            house.image = result.data!!.data!!
                            Picasso.get()
                                .load(house.image)
                                .into(binding.houseImage)
                            binding.chooseImage.setText(R.string.change_houseImage)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun doIrreversibleStuffOrCancel() {
        alert {
            messageResource = R.string.dialog_msg_confirm_irreversible_stuff
            okButton {
                app.houses.delete(house)
                setResult(RESULT_OK)
                finish()
            }
            cancelButton()
        }.onShow {
        }.show()
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            house.lat = location.lat
                            house.lng = location.lng
                            house.zoom = location.zoom
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

}