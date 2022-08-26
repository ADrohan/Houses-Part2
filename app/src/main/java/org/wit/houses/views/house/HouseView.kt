package org.wit.houses.views.house

import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.houses.R
import org.wit.houses.databinding.ActivityHouseBinding
import org.wit.houses.models.HouseModel
import splitties.alertdialog.*
import timber.log.Timber.i
import java.text.SimpleDateFormat
import java.util.*


class HouseView : AppCompatActivity() {

    private lateinit var binding: ActivityHouseBinding
    private lateinit var presenter: HousePresenter
    var house = HouseModel()
    var calList = Calendar.getInstance()
    var calSold = Calendar.getInstance()

    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        binding.mapView2.onCreate(savedInstanceState)
        binding.mapView2.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation()}
        }

        presenter = HousePresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cachePlacemark(
                binding.houseAddress.text.toString(),
                binding.description.text.toString(),
                binding.bedrooms.text.toString(),
                binding.bathrooms.text.toString(),
                binding.soldPrice.text.toString(),
                binding.listPrice.text.toString(),
                binding.listDate.text.toString(),
                binding.soldDate.text.toString()
            )
            presenter.doSelectImage()
        }

        binding.placemarkLocation.setOnClickListener {
            presenter.cachePlacemark(
                binding.houseAddress.text.toString(),
                binding.description.text.toString(),
                binding.bedrooms.text.toString(),
                binding.bathrooms.text.toString(),
                binding.soldPrice.text.toString(),
                binding.listPrice.text.toString(),
                binding.listDate.text.toString(),
                binding.soldDate.text.toString()
            )
            presenter.doSetLocation()
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calList.set(Calendar.YEAR, year)
                calList.set(Calendar.MONTH, month)
                calList.set(Calendar.DAY_OF_MONTH, day)
                updateListDate()
            }
        val dateSoldListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calSold.set(Calendar.YEAR, year)
                calSold.set(Calendar.MONTH, month)
                calSold.set(Calendar.DAY_OF_MONTH, day)
                updateSoldDate()
            }

        binding.selectListDate.setOnClickListener {
            DatePickerDialog(
                this@HouseView, dateSetListener,
                calList.get(Calendar.YEAR),
                calList.get(Calendar.MONTH),
                calList.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.selectSoldDate.setOnClickListener {
            DatePickerDialog(
                this@HouseView, dateSoldListener,
                calSold.get(Calendar.YEAR),
                calSold.get(Calendar.MONTH),
                calSold.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_house, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit) {
            deleteMenu.setVisible(true)
        } else {
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_save -> {
                if (binding.houseAddress.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_houseAddress, Snackbar.LENGTH_LONG)
                        .show()
                } else if (binding.listPrice.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_listPrice, Snackbar.LENGTH_LONG)
                        .show()
                }
                else {
                    GlobalScope.launch(Dispatchers.IO) {
                        presenter.doAddOrSave(
                            binding.houseAddress.text.toString(),
                            binding.description.text.toString(),
                            binding.bathrooms.text.toString(),
                            binding.bedrooms.text.toString(),
                            binding.soldPrice.text.toString(),
                            binding.listPrice.text.toString(),
                            binding.listDate.text.toString(),
                            binding.soldDate.text.toString()
                        )
                    }
                }
            }
            R.id.item_delete -> {
                alert {
                    messageResource = R.string.dialog_msg_confirm_irreversible_stuff
                    okButton {
                        GlobalScope.launch(Dispatchers.IO) {
                            presenter.doDelete()
                        }
                    }
                    cancelButton()
                }.onShow {}.show()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_logout -> { presenter.doLogout() }

        }
        return super.onOptionsItemSelected(item)
    }

    fun showPlacemark(house: HouseModel) {
        if (binding.houseAddress.text.isEmpty()) binding.houseAddress.setText(house.address)
        if (binding.description.text.isEmpty()) binding.description.setText(house.description)
        if (binding.bedrooms.text.isEmpty()) binding.bedrooms.setText(house.bedrooms)
        if (binding.bathrooms.text.isEmpty()) binding.bathrooms.setText(house.bathrooms)
        if (binding.listPrice.text.isEmpty()) binding.listPrice.setText(house.listPrice)
        if (binding.soldPrice.text.isEmpty()) binding.soldPrice.setText(house.soldPrice)
        if (binding.listDate.text.isEmpty())  binding.listDate.setText(house.listDate)
        if (binding.soldDate.text.isEmpty())  binding.soldDate.setText(house.soldDate)

        binding.lat.setText("%.6f".format(house.location.lat))
        binding.lng.setText("%.6f".format(house.location.lng))

        Picasso.get()
            .load(house.image)
            .placeholder(R.drawable.orange_house)
            .into(binding.houseImage)
        if (house.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_houseImage)
        } else { binding.chooseImage.setText(R.string.button_addImage)
        }
        if (house.listDate.isEmpty()) {
            binding.selectListDate.setText(R.string.button_listDate)
        } else {binding.selectListDate.setText(R.string.change_list_date)
        }
        if (house.soldDate.isEmpty()) {
            binding.selectSoldDate.setText(R.string.button_soldDate)}
        else { binding.selectSoldDate.setText(R.string.change_sold_date)
        }
    }

    fun updateImage(image: Uri) {
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.houseImage)
        binding.chooseImage.setText(R.string.change_houseImage)
    }

    private fun updateListDate() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale.getDefault())
        binding.listDate.text = sdf.format(calList.time)
    }

    private fun updateSoldDate() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale.getDefault())
        binding.soldDate.text = sdf.format(calSold.time)

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
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView2.onSaveInstanceState(outState)
    }
}