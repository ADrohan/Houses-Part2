package org.wit.houses.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}
class HouseMemStore : HouseStore {

    val houses = ArrayList<HouseModel>()

    override fun findAll(): List<HouseModel> {
        return houses
    }

    override fun findById(id:Long) : HouseModel? {
        val foundHouse: HouseModel? = houses.find { it.id == id }
        return foundHouse
    }

    override fun create(house: HouseModel) {
        house.id = getId()
        houses.add(house)
        logAll()
    }

    override fun update(house: HouseModel) {
        var foundHouse: HouseModel? = houses.find { p -> p.id == house.id}
        if (foundHouse != null) {
            foundHouse.address = house.address
            foundHouse.listPrice = house.listPrice
            foundHouse.bedrooms = house.bedrooms
            foundHouse.bathrooms = house.bathrooms
            foundHouse.description = house.description
            foundHouse.soldPrice = house.soldPrice
            foundHouse.auctioneer= house.auctioneer
            foundHouse.image = house.image
            foundHouse.lat = house.lat
            foundHouse.lng = house.lng
            foundHouse.zoom = house.zoom
            logAll()
        }
    }

    override fun delete(house: HouseModel) {
            houses.remove(house)
            logAll()
    }

    fun logAll() {
        houses.forEach{ i("${it}") }
    }
}