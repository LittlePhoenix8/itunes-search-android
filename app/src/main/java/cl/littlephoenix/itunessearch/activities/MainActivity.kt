package cl.littlephoenix.itunessearch.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cl.littlephoenix.itunessearch.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), SearchView.OnQueryTextListener
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents()
    {
        //set support bar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        //set navigation
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfig = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.itemSearch)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    //OnQueryTextListener
    override fun onQueryTextSubmit(query: String?): Boolean
    {
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean
    {
        //TODO here
        return false
    }

}
