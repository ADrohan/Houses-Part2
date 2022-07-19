package org.wit.houses.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.wit.houses.helpers.Converters

import org.wit.houses.models.HouseModel

@Database(entities = arrayOf(HouseModel::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)

abstract class Database : RoomDatabase() {
    abstract fun houseDao(): HouseDao
}