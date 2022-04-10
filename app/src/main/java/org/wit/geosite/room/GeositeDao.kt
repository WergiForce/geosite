package org.wit.geosite.room

import androidx.room.*
import org.wit.geosite.models.GeositeModel

@Dao
interface GeositeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(geosite: GeositeModel)

    @Query("SELECT * FROM GeositeModel")
    suspend fun findAll(): List<GeositeModel>

    @Query("select * from GeositeModel where id = :id")
    suspend fun findById(id: Long): GeositeModel

    @Update
    suspend fun update(geosite: GeositeModel)

    @Delete
    suspend fun deleteGeosite(geosite: GeositeModel)
}