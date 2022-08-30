package org.wit.houses.models

import android.content.Context
//import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
//import com.google.firebase.storage.StorageReference
//import org.wit.houses.helpers.readImageFromPath
//import java.io.ByteArrayOutputStream
//import java.io.File
import kotlin.collections.ArrayList
//import timber.log.Timber.i

class HouseFireStore(val context: Context) : HouseStore {
    val houses = ArrayList<HouseModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    //lateinit var st: StorageReference

    override suspend fun findAll(): List<HouseModel> {
        return houses
    }

    // find by id no longer works as the id is always 0
    override suspend fun findById(id: Long): HouseModel? {
        val foundHouse: HouseModel? = houses.find { p -> p.id == id }
        return foundHouse
    }

    override suspend fun create(house: HouseModel) {
        val key = db.child("users").child(userId).child("houses").push().key
        key?.let {
            house.fbId = key
            houses.add(house)
            db.child("users").child(userId).child("houses").child(key).setValue(house)
          //  updateImage(house)
        }
    }

    override suspend fun update(house: HouseModel) {
        var foundHouse: HouseModel? = houses.find { p -> p.fbId == house.fbId }
        if (foundHouse != null) {
            foundHouse.address = house.address
            foundHouse.listPrice = house.listPrice
            foundHouse.bedrooms = house.bedrooms
            foundHouse.bathrooms = house.bathrooms
            foundHouse.description = house.description
            foundHouse.soldPrice = house.soldPrice
            foundHouse.image = house.image
            foundHouse.location = house.location
            foundHouse.listDate = house.listDate
            foundHouse.soldDate = house.soldDate
        }

        db.child("users").child(userId).child("houses").child(house.fbId).setValue(house)
    //    if(house.image.length > 0) {
    //        updateImage(house)
    //   }
    }

    override suspend fun delete(house: HouseModel) {
        db.child("users").child(userId).child("houses").child(house.fbId).removeValue()
       houses.remove(house)
    }

    override suspend fun clear() {
        houses.clear()
    }

    fun fetchHouses(housesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(houses) {
                    it.getValue<HouseModel>(
                        HouseModel::class.java
                    )
                }
                housesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        houses.clear()
        db.child("users").child(userId).child("houses")
            .addListenerForSingleValueEvent(valueEventListener)
    }
    /*
    fun updateImage(house: HouseModel){
        if(house.image != ""){
            val fileName = File(house.image)
            val imageName = fileName.getName()
            i("Image name is: " + imageName)

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, house.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)

                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        house.image = it.toString()
                        db.child("users").child(userId).child("houses").child(house.fbId).setValue(house)
                    }
                }.addOnFailureListener{
                    var errorMessage = it.message
                    i("Failure: $errorMessage")
                }
            }

        }
    }

     */
}