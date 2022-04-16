package org.wit.geosite.models

import android.content.Context
import com.google.firebase.database.*

class GeositeFireStore(val context: Context) : GeositeStore {

    val geosites = ArrayList<GeositeModel>()
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<GeositeModel> {
        return geosites
    }

    override suspend fun findById(id: Long): GeositeModel? {
        val foundGeosite: GeositeModel? = geosites.find { p -> p.id == id }
        return foundGeosite
    }

    override suspend fun create(geosite: GeositeModel) {
        val key = db.child("geosites").push().key
        key?.let {
            geosite.fbId = key
            geosites.add(geosite)
            db.child("geosites").child(key).setValue(geosite)
        }
    }

    override suspend fun update(geosite: GeositeModel) {
        var foundGeosite: GeositeModel? = geosites.find { p -> p.fbId == geosite.fbId }
        if (foundGeosite != null) {
            foundGeosite.title = geosite.title
            foundGeosite.description = geosite.description
            foundGeosite.landowner = geosite.landowner
            foundGeosite.phone = geosite.phone
            foundGeosite.drilling = geosite.drilling
            foundGeosite.comment = geosite.comment
            foundGeosite.image = geosite.image
            foundGeosite.location = geosite.location
        }

        db.child("geosites").child(geosite.fbId).setValue(geosite)

    }

    override suspend fun delete(geosite: GeositeModel) {
        db.child("geosites").child(geosite.fbId).removeValue()
        geosites.remove(geosite)
    }

    override suspend fun clear() {
        geosites.clear()
    }

    fun fetchGeosites(geositesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(geosites) {
                    it.getValue<GeositeModel>(
                        GeositeModel::class.java
                    )
                }
                geositesReady()
            }
        }
        db = FirebaseDatabase.getInstance("https://hdip-geosite-default-rtdb.firebaseio.com/").reference
        geosites.clear()
        db.child("geosites")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}
