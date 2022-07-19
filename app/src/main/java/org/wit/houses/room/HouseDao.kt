package org.wit.houses.room

import androidx.room.*
import org.wit.houses.models.HouseModel

@Dao
interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(house: HouseModel)

    @Query("SELECT * FROM HouseModel")
    suspend fun findAll(): List<HouseModel>

    @Query("select * from HouseModel where id = :id")
    suspend fun findById(id: Long): HouseModel

    @Update
    suspend fun update(house: HouseModel)

    @Delete
    suspend fun deletePlacemark(house: HouseModel)
}
