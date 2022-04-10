package org.wit.geosite.views.geosite

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.geosite.R
import org.wit.geosite.databinding.ActivityGeositeBinding
import org.wit.geosite.models.GeositeModel
import org.wit.geosite.models.Location
import timber.log.Timber.i

class GeositeView : AppCompatActivity() {

    private lateinit var binding: ActivityGeositeBinding
    private lateinit var presenter: GeositePresenter
    lateinit var map: GoogleMap
    var geosite = GeositeModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGeositeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = GeositePresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheGeosite(binding.geositeTitle.text.toString(), binding.description.text.toString(), binding.ightheme.selectedItem.toString())
            presenter.doSelectImage()
        }

        binding.mapView2.setOnClickListener {
            presenter.cacheGeosite(binding.geositeTitle.text.toString(), binding.description.text.toString(), binding.ightheme.selectedItem.toString())
            presenter.doSetLocation()
        }

        binding.mapView2.onCreate(savedInstanceState);
        binding.mapView2.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }


        val spinner: Spinner = findViewById(R.id.ightheme)
        ArrayAdapter.createFromResource(
            this,
            R.array.ighthemes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_geosite, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.geositeTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_geosite_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        presenter.doAddOrSave(
                            binding.geositeTitle.text.toString(),
                            binding.description.text.toString(),
                            binding.ightheme.selectedItem.toString()
                        )
                    }
                }
            }
            R.id.item_delete -> {
                GlobalScope.launch(Dispatchers.IO){
                    presenter.doDelete()
                }
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun showGeosite(geosite: GeositeModel) {
        if (binding.geositeTitle.text.isEmpty()) binding.geositeTitle.setText(geosite.title)
        if (binding.description.text.isEmpty())  binding.description.setText(geosite.description)
        if (geosite.image != "") {
            Picasso.get()
                .load(geosite.image)
                .into(binding.geositeImage)

            binding.chooseImage.setText(R.string.change_geosite_image)
        }
        this.showLocation(geosite.location)
    }

    private fun showLocation (loc: Location){
        binding.lat.setText("%.6f".format(loc.lat))
        binding.lng.setText("%.6f".format(loc.lng))
    }

    fun updateImage(image: String){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.geositeImage)
        binding.chooseImage.setText(R.string.change_geosite_image)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView2.onSaveInstanceState(outState)
    }

}