package org.wit.geosite.views.geositelist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import org.wit.geosite.R
import org.wit.geosite.adapters.GeositeAdapter
import org.wit.geosite.adapters.GeositeListener
import org.wit.geosite.databinding.ActivityGeositeListBinding
import org.wit.geosite.main.MainApp
import org.wit.geosite.models.GeositeModel
import timber.log.Timber.i

class GeositeListView : AppCompatActivity(), GeositeListener {

    lateinit var app: MainApp
    lateinit var binding: ActivityGeositeListBinding
    lateinit var presenter: GeositeListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityGeositeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.toolbar.title = "${title}: ${user.email}"
        }

        setSupportActionBar(binding.toolbar)
        presenter = GeositeListPresenter(this)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        updateRecyclerView()

        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener {
            presenter.doAddGeosite()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddGeosite() }
            R.id.item_map -> { presenter.doShowGeositesMap() }
            R.id.item_logout -> {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doLogout()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGeositeClick(geosite: GeositeModel) {
        presenter.doEditGeosite(geosite)
    }



    private fun updateRecyclerView(){
        GlobalScope.launch(Dispatchers.Main){
            binding.recyclerView.adapter =
                GeositeAdapter(presenter.getGeosites(), this@GeositeListView)
        }
    }

}