package org.wit.geosite.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GeositeMemStore : GeositeStore {

    val geosites = ArrayList<GeositeModel>()

    override suspend fun findAll(): List<GeositeModel> {
        return geosites
    }

    override suspend fun create(geosite: GeositeModel) {
        geosite.id = getId()
        geosites.add(geosite)
        logAll()
    }

    override suspend fun update(geosite: GeositeModel) {
        val foundGeosite: GeositeModel? = geosites.find { p -> p.id == geosite.id }
        if (foundGeosite != null) {
            foundGeosite.title = geosite.title
            foundGeosite.description = geosite.description
            foundGeosite.landowner = geosite.landowner
            foundGeosite.phone = geosite.phone
            foundGeosite.drilling = geosite.drilling
            foundGeosite.comment = geosite.comment
            foundGeosite.image = geosite.image
            foundGeosite.location = geosite.location
            logAll()
        }
    }
    override suspend fun delete(geosite: GeositeModel) {
        geosites.remove(geosite)
        logAll()
    }

    private fun logAll() {
        geosites.forEach { i("$it") }
    }
    override suspend fun findById(id:Long) : GeositeModel? {
        val foundGeosite: GeositeModel? = geosites.find { it.id == id }
        return foundGeosite
    }

    override suspend fun clear(){
        geosites.clear()
    }
}