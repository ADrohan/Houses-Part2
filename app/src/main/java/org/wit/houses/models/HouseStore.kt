package org.wit.houses.models

interface HouseStore {
    fun findAll(): List<HouseModel>
    fun create(house: HouseModel)
    fun update(house: HouseModel)
    fun delete(house: HouseModel)
    fun findById(id:Long) : HouseModel?
}