package org.wit.geosite.models

interface GeositeStore {
    suspend fun findAll(): List<GeositeModel>
    suspend fun create(geosite: GeositeModel)
    suspend fun update(geosite: GeositeModel)
    suspend fun findById(id:Long) : GeositeModel?
    suspend fun delete(geosite: GeositeModel)
    suspend fun clear()
}