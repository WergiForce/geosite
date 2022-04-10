package org.wit.geosite.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.wit.geosite.helpers.Converters
import org.wit.geosite.models.GeositeModel

@Database(entities = arrayOf(GeositeModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun geositeDao(): GeositeDao
}