package org.wit.houses.views.house

import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

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

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                //TODO("Not yet implemented")
                calList.set(Calendar.YEAR, year)
                calList.set(Calendar.MONTH, month)
                calList.set(Calendar.DAY_OF_MONTH, day)
                   updateListDate()
            }
        }

        binding.selectListDate.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                // TODO("Not yet implemented")
                DatePickerDialog(this@HouseView, dateSetListener,
                    calList.get(Calendar.YEAR),
                    calList.get(Calendar.MONTH),
                    calList.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })


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
            R.id.item_delete -> {
                alert {
                    messageResource = R.string.dialog_msg_confirm_irreversible_stuff
                    okButton { presenter.doDelete() }
                    cancelButton()
                }.onShow {}.show()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showPlacemark(house: HouseModel) {
        binding.houseAddress.setText(house.address)
        binding.listPrice.setText(house.listPrice)
        binding.bedrooms.setText(house.bedrooms)
        binding.bathrooms.setText(house.bathrooms)
        binding.description.setText(house.description)
        binding.soldPrice.setText(house.soldPrice)
        binding.listDate.setText(house.listDate)
        binding.soldDate.setText(house.soldDate)
      //  binding.btnAdd.setText(R.string.Update_house)
        Picasso.get()
            .load(house.image)
            .into(binding.houseImage)
        if (house.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_houseImage)
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
}