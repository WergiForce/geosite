package org.wit.geosite.views.geosite

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.geosite.helpers.checkLocationPermissions
import org.wit.geosite.helpers.createDefaultLocationRequest
import org.wit.geosite.main.MainApp
import org.wit.geosite.models.Location
import org.wit.geosite.models.GeositeModel
import org.wit.geosite.showImagePicker
import org.wit.geosite.views.location.EditLocationView
import timber.log.Timber
import timber.log.Timber.i

class GeositePresenter(private val view: GeositeView) {
    private val locationRequest = createDefaultLocationRequest()
    var map: GoogleMap? = null
    var geosite = GeositeModel()
    var app: MainApp = view.application as MainApp
    var locationManualyChanged = false;
    //location service
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)

    init {

        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("geosite_edit")) {
            edit = true
            geosite = view.intent.extras?.getParcelable("geosite_edit")!!
            view.showGeosite(geosite)
        }
        else {

            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            geosite.location.lat = location.lat
            geosite.location.lng = location.lng
        }

    }


    suspend fun doAddOrSave(title: String, description: String, landowner: String,
                            phone: String, drilling: String, comment: String) {
        geosite.title = title
        geosite.description = description
        geosite.landowner = landowner
        geosite.phone = phone
        geosite.drilling = drilling
        geosite.comment = comment
        if (edit) {
            app.geosites.update(geosite)
        } else {
            app.geosites.create(geosite)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    suspend fun doDelete() {
        app.geosites.delete(geosite)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {
        locationManualyChanged = true;

        if (geosite.location.zoom != 0f) {

            location.lat =  geosite.location.lat
            location.lng = geosite.location.lng
            location.zoom = geosite.location.zoom
            locationUpdate(geosite.location.lat, geosite.location.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {

        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if(!locationManualyChanged){
                        locationUpdate(l.latitude, l.longitude)
                    }
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(geosite.location.lat, geosite.location.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        geosite.location = location
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(geosite.title).position(LatLng(geosite.location.lat, geosite.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(geosite.location.lat, geosite.location.lng), geosite.location.zoom))
        view.showGeosite(geosite)
    }

    fun cacheGeosite (title: String, description: String, landowner: String,
                      phone: String, drilling: String, comment: String) {
        geosite.title = title
        geosite.description = description
        geosite.landowner = landowner
        geosite.phone = phone
        geosite.drilling = drilling
        geosite.comment = comment
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            geosite.image = result.data!!.data!!.toString()
                            view.updateImage(geosite.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            geosite.location = location
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun doPermissionLauncher() {
        i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}