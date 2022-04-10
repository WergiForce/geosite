package org.wit.geosite.models

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.wit.geosite.readImageFromPath
import timber.log.Timber.i
import java.io.ByteArrayOutputStream
import java.io.File

class GeositeFireStore(val context: Context) : GeositeStore {
    val geosites = ArrayList<GeositeModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    override suspend fun findAll(): List<GeositeModel> {
        return geosites
    }

    override suspend fun findById(id: Long): GeositeModel? {
        val foundGeosite: GeositeModel? = geosites.find { p -> p.id == id }
        return foundGeosite
    }

    override suspend fun create(geosite: GeositeModel) {
        val key = db.child("users").child(userId).child("geosites").push().key
        key?.let {
            geosite.fbId = key
            geosites.add(geosite)
            db.child("users").child(userId).child("geosites").child(key).setValue(geosite)
            updateImage(geosite)
        }
    }

    override suspend fun update(geosite: GeositeModel) {
        var foundGeosite: GeositeModel? = geosites.find { p -> p.fbId == geosite.fbId }
        if (foundGeosite != null) {
            foundGeosite.title = geosite.title
            foundGeosite.description = geosite.description
            foundGeosite.ightheme = geosite.ightheme
            foundGeosite.image = geosite.image
            foundGeosite.location = geosite.location
        }

        db.child("users").child(userId).child("geosites").child(geosite.fbId).setValue(geosite)
        if(geosite.image.length > 0){
            updateImage(geosite)
        }
    }

    override suspend fun delete(geosite: GeositeModel) {
        db.child("users").child(userId).child("geosites").child(geosite.fbId).removeValue()
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
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        st = FirebaseStorage.getInstance().reference
        db = FirebaseDatabase.getInstance("https://hdip-geosite-default-rtdb.firebaseio.com/").reference
        geosites.clear()
        db.child("users").child(userId).child("geosites")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImage(geosite: GeositeModel){
        if(geosite.image != ""){
            val fileName = File(geosite.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, geosite.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)

                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        geosite.image = it.toString()
                        db.child("users").child(userId).child("geosites").child(geosite.fbId).setValue(geosite)
                    }
                }.addOnFailureListener{
                    var errorMessage = it.message
                    i("Failure: $errorMessage")
                }
            }

        }
    }
}