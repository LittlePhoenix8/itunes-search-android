package cl.littlephoenix.itunessearch.fragments.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModelStores
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation

import cl.littlephoenix.itunessearch.R
import cl.littlephoenix.itunessearch.adapters.DetailAdapter
import cl.littlephoenix.itunessearch.adapters.SongAdapter
import cl.littlephoenix.itunessearch.helpers.DataHelper
import cl.littlephoenix.itunessearch.interfaces.OnSongSelectListener
import cl.littlephoenix.itunessearch.models.BaseResponse
import cl.littlephoenix.itunessearch.models.response.DetailResponse
import cl.littlephoenix.itunessearch.models.response.SongsResponse
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(), OnSongSelectListener
{
    private lateinit var viewModel: DetailViewModel
    private val details = ArrayList<DetailResponse>()
    private val songs = ArrayList<SongsResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?)
    {
        super.onPrepareOptionsMenu(menu)
        menu?.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents()
    {
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.getArtistDetail().observe(this, ArtistObserverResponse())
        viewModel.getArtistSongs().observe(this, SongsObserverResponse())
        viewModel.getError().observe(this, ErrorObserverResponse())

        recyclerDetails.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
        recyclerDetails.adapter = DetailAdapter(details)

        recyclerSongs.layoutManager = LinearLayoutManager(context)
        recyclerSongs.adapter = SongAdapter(songs, this)

        showProgressBar(true)

        val idArtist = arguments?.getString("id_artist")
        idArtist?.let {
            viewModel.getArtistDetail(it)
            viewModel.getArtistSongs(it)
        }?: run {
            showToastMessage(getString(R.string.error_id))
            navigateUp()
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

    private fun navigateUp()
    {
        view?.let {
            val navController = Navigation.findNavController(it)
            navController.navigateUp()
        }
    }

    override fun playSongAt(position: Int)
    {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class ArtistObserverResponse: Observer<BaseResponse<DetailResponse>>
    {
        override fun onChanged(t: BaseResponse<DetailResponse>?)
        {
            ViewModelStores.of(this@DetailFragment).clear()
            showProgressBar(false)
            t?.results?.let {
                details.clear()
                details.addAll(DataHelper().parseDetailData(it))
                recyclerDetails.adapter?.notifyDataSetChanged()
            }
        }
    }

    inner class SongsObserverResponse: Observer<BaseResponse<SongsResponse>>
    {
        override fun onChanged(t: BaseResponse<SongsResponse>?)
        {
            ViewModelStores.of(this@DetailFragment).clear()
            showProgressBar(false)
            t?.results?.let {
                songs.clear()
                songs.addAll(DataHelper().parseSongsData(it))
                recyclerSongs.adapter?.notifyDataSetChanged()
            }
        }
    }

    inner class ErrorObserverResponse: Observer<String>
    {
        override fun onChanged(t: String?)
        {
            ViewModelStores.of(this@DetailFragment).clear()
            showProgressBar(false)
            t?.let {
                showToastMessage(it)
            }
        }
    }
}
