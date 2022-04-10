package org.wit.geosite.room

import android.content.Context
import androidx.room.Room
import org.wit.geosite.models.GeositeModel
import org.wit.geosite.models.GeositeStore

class GeositeStoreRoom(val context: Context) : GeositeStore {

    var dao: GeositeDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.geositeDao()
    }

    override suspend fun findAll(): List<GeositeModel> {

        return dao.findAll()
    }

    override suspend fun findById(id: Long): GeositeModel? {
        return dao.findById(id)
    }

    override suspend fun create(geosite: GeositeModel) {
        dao.create(geosite)
    }

    override suspend fun update(geosite: GeositeModel) {
        dao.update(geosite)
    }

    override suspend fun delete(geosite: GeositeModel) {
        dao.deleteGeosite(geosite)
    }

    override suspend fun clear() {
    }
}