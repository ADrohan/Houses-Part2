package org.wit.houses.models

interface HouseStore {
    suspend fun findAll(): List<HouseModel>
    suspend fun create(house: HouseModel)
    suspend fun update(house: HouseModel)
    suspend fun delete(house: HouseModel)
    suspend fun findById(id:Long) : HouseModel?
    suspend fun clear()
}