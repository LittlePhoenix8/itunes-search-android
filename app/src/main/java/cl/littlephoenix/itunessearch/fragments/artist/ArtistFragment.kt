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

import cl.littlephoenix.itunessearch.R
import cl.littlephoenix.itunessearch.activities.MainActivity
import cl.littlephoenix.itunessearch.adapters.ArtistAdapter
import cl.littlephoenix.itunessearch.interfaces.OnArtistSelectListener
import cl.littlephoenix.itunessearch.interfaces.OnSearchListener
import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.ArtistResponse
import cl.littlephoenix.itunessearch.persistence.SearchSharedPreferences
import kotlinx.android.synthetic.main.fragment_artist.*

class ArtistFragment : Fragment(), OnArtistSelectListener, OnSearchListener
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

        activity?.let {
            if(it is MainActivity)
            {
                it.addOnSearchListener(this)
            }
        }

        recyclerArtists.layoutManager = LinearLayoutManager(context)
        recyclerArtists.adapter = ArtistAdapter(artist, this)

        showProgressBar()
        SearchSharedPreferences(context!!).getLastSearch()?.let {
            viewModel.searchArtist(it)
        }?: run {
            viewModel.searchArtist("a")
        }
    }

    private fun showProgressBar()
    {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar()
    {
        progressBar.visibility = View.GONE
    }

    private fun showToastMessage(message: String)
    {
        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
    }

    override fun onArtistSelectedAt(position: Int)
    {
        //TODO artist detail
    }

    override fun onSearchEnter(query: String?)
    {
        query?.let {
            SearchSharedPreferences(context!!).setLastSearch(it)
            showProgressBar()
            viewModel.searchArtist(it)
        }
    }

    inner class ArtistObserverResponse: Observer<BaseResponse<ArtistResponse>>
    {
        override fun onChanged(t: BaseResponse<ArtistResponse>?)
        {
            ViewModelStores.of(this@ArtistFragment).clear()
            hideProgressBar()
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
            hideProgressBar()
            t?.let {
                showToastMessage(it)
            }
        }
    }
}
