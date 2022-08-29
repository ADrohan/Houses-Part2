package org.wit.houses.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class HouseFireStore(val context: Context) : HouseStore {
    val houses = ArrayList<HouseModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

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
}