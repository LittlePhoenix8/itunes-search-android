package cl.littlephoenix.itunessearch.fragments.artist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModelStores
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation

import cl.littlephoenix.itunessearch.R
import cl.littlephoenix.itunessearch.activities.MainActivity
import cl.littlephoenix.itunessearch.adapters.ArtistAdapter
import cl.littlephoenix.itunessearch.interfaces.OnArtistSelectListener
import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.ArtistResponse
import cl.littlephoenix.itunessearch.persistence.SearchSharedPreferences
import kotlinx.android.synthetic.main.fragment_artist.*

class ArtistFragment : Fragment(), OnArtistSelectListener
{
    private lateinit var viewModel: ArtistViewModel
    private val artist = ArrayList<ArtistResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents()
    {
        viewModel = ViewModelProviders.of(this).get(ArtistViewModel::class.java)
        viewModel.getArtists().observe(this, ArtistObserverResponse())
        viewModel.getError().observe(this, ErrorObserverResponse())
        viewModel.savedQuery().observe(this, QueryObserverResponse())
        viewModel.showProgress().observe(this, ProgressObserverResponse())

        activity?.let {
            if(it is MainActivity)
            {
                it.getController().artistViewModel = viewModel
            }
        }

        recyclerArtists.layoutManager = LinearLayoutManager(context)
        recyclerArtists.adapter = ArtistAdapter(artist, this)

        SearchSharedPreferences(context!!).getLastSearch()?.let {
            viewModel.searchArtist(it)
        }?: run {
            viewModel.searchArtist("a")
        }
    }

    private fun showProgressBar(show: Boolean)
    {
        if(show)
        {
            progressBar.visibility = View.VISIBLE
        }
        else
        {
            progressBar.visibility = View.GONE
        }
    }

    private fun showToastMessage(message: String)
    {
        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
    }

    override fun onArtistSelectedAt(position: Int)
    {
        view?.let {
            val bundle = Bundle()
            bundle.putString("artist_name", artist[position].artistName)
            bundle.putString("id_artist", artist[position].amgArtistId.toString())
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.artist_to_detail, bundle)
        }
    }

    inner class ArtistObserverResponse: Observer<BaseResponse<ArtistResponse>>
    {
        override fun onChanged(t: BaseResponse<ArtistResponse>?)
        {
            ViewModelStores.of(this@ArtistFragment).clear()
            showProgressBar(false)
            t?.results?.let {
                artist.clear()
                artist.addAll(it)
                recyclerArtists.adapter?.notifyDataSetChanged()
            }
        }
    }

    inner class ErrorObserverResponse: Observer<String>
    {
        override fun onChanged(t: String?)
        {
            ViewModelStores.of(this@ArtistFragment).clear()
            showProgressBar(false)
            t?.let {
                showToastMessage(it)
            }
        }
    }

    inner class QueryObserverResponse: Observer<String>
    {
        override fun onChanged(t: String?)
        {
            ViewModelStores.of(this@ArtistFragment).clear()
            t?.let {
                SearchSharedPreferences(context!!).setLastSearch(it)
            }
        }
    }

    inner class ProgressObserverResponse: Observer<Boolean>
    {
        override fun onChanged(t: Boolean?)
        {
            ViewModelStores.of(this@ArtistFragment).clear()
            t?.let {
                showProgressBar(it)
            }
        }
    }
}
