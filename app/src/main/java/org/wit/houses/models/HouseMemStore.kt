package org.wit.houses.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}
class HouseMemStore : HouseStore {

    val houses = ArrayList<HouseModel>()

    override suspend fun findAll(): List<HouseModel> {
        return houses
    }

    override suspend fun findById(id:Long) : HouseModel? {
        val foundHouse: HouseModel? = houses.find { it.id == id }
        return foundHouse
    }

    override suspend fun create(house: HouseModel) {
        house.id = getId()
        houses.add(house)
        logAll()
    }

    override suspend fun update(house: HouseModel) {
        var foundHouse: HouseModel? = houses.find { p -> p.id == house.id}
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
            logAll()
        }
    }

    override suspend fun delete(house: HouseModel) {
            houses.remove(house)
            logAll()
    }

    private fun logAll() {
        houses.forEach{ i("${it}") }
    }
    override suspend fun clear(){
        houses.clear()
    }
}