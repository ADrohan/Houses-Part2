package org.wit.houses.room

import android.content.Context
import androidx.room.Room
import org.wit.houses.models.HouseModel
import org.wit.houses.models.HouseStore

class HouseStoreRoom(val context: Context) : HouseStore {

    var dao: HouseDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.houseDao()
    }

    override suspend fun findAll(): List<HouseModel> {
        return dao.findAll()
    }

    override suspend fun findById(id: Long): HouseModel? {
        return dao.findById(id)
    }

    override suspend fun create(house: HouseModel) {
        dao.create(house)
    }

    override suspend fun update(house: HouseModel) {
        dao.update(house)
    }

    override suspend fun delete(house: HouseModel) {
        dao.deletePlacemark(house)
    }

    override suspend fun clear() {
    }
}