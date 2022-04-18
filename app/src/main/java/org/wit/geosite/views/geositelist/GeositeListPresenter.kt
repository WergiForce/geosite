package org.wit.geosite.views.geositelist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.geosite.main.MainApp
import org.wit.geosite.models.GeositeModel
import org.wit.geosite.views.geosite.GeositeView
import org.wit.geosite.views.login.LoginView
import org.wit.geosite.views.map.GeositeMapView

class GeositeListPresenter(val view: GeositeListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    suspend fun getGeosites() = app.geosites.findAll()

    fun doAddGeosite() {
        val launcherIntent = Intent(view, GeositeView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditGeosite(geosite: GeositeModel) {
        val launcherIntent = Intent(view, GeositeView::class.java)
        launcherIntent.putExtra("geosite_edit", geosite)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowGeositesMap() {
        val launcherIntent = Intent(view, GeositeMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getGeosites()
                }
            }
    }

    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }

    suspend fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        app.geosites.clear()
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
}