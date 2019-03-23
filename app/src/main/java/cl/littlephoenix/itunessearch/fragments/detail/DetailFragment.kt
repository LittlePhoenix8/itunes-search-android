package cl.littlephoenix.itunessearch.fragments.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.lifecycle.ViewModelStores
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
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
import java.lang.Exception
import android.media.AudioAttributes
import android.os.Build

class DetailFragment : Fragment(), OnSongSelectListener, MediaPlayer.OnCompletionListener,
                       View.OnClickListener
{
    private lateinit var viewModel: DetailViewModel
    private var mediaPlayer: MediaPlayer? = null
    private val details = ArrayList<DetailResponse>()
    private val songs = ArrayList<SongsResponse>()
    private var currentPlaying: Int = -1

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

    override fun onDetach()
    {
        super.onDetach()
        mediaPlayer?.release()
    }

    @Suppress("DEPRECATION")
    private fun initComponents()
    {
        mediaPlayer = MediaPlayer()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            val aa = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            mediaPlayer?.setAudioAttributes(aa)
        }
        else
        {
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.getArtistDetail().observe(this, ArtistObserverResponse())
        viewModel.getArtistSongs().observe(this, SongsObserverResponse())
        viewModel.getError().observe(this, ErrorObserverResponse())

        recyclerDetails.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
        recyclerDetails.adapter = DetailAdapter(details)

        recyclerSongs.layoutManager = LinearLayoutManager(context)
        recyclerSongs.adapter = SongAdapter(songs, this)
        recyclerSongs.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))

        btnPreview.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        btnStop.setOnClickListener(this)

        val idArtist = arguments?.getString("id_artist")
        idArtist?.let {
            showProgressBar(true)
            showPlayer(false)
            viewModel.getArtistDetail(it)
            viewModel.getArtistSongs(it)
        }?: run {
            showToastMessage(getString(R.string.error_id))
            navigateUp()
        }
    }

    private fun showToastMessage(message: String)
    {
        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
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

    //Player Configuration
    private fun showPlayer(show: Boolean)
    {
        if(show)
        {
            playLayout.visibility = View.VISIBLE
        }
        else
        {
            playLayout.visibility = View.GONE
        }
    }

    private fun setSelected(oldPosition: Int, newPosition: Int)
    {
        if(oldPosition != -1)
        {
            songs[oldPosition].isPlaying = false
        }
        songs[newPosition].isPlaying = true
        recyclerSongs.adapter?.notifyDataSetChanged()

        when(newPosition)
        {
            0 ->
            {
                btnPreview.visibility = View.INVISIBLE
                btnNext.visibility = View.VISIBLE
            }
            songs.size - 1 ->
            {
                btnPreview.visibility = View.VISIBLE
                btnNext.visibility = View.INVISIBLE
            }
            else ->
            {
                btnPreview.visibility = View.VISIBLE
                btnNext.visibility = View.VISIBLE
            }
        }
    }

    private fun setPlay()
    {
        btnPlay.setImageResource(R.drawable.baseline_pause_black_36)
        showProgressBar(false)
    }

    private fun setPause()
    {
        btnPlay.setImageResource(R.drawable.baseline_play_arrow_white_36)
    }

    private fun navigateUp()
    {
        view?.let {
            val navController = Navigation.findNavController(it)
            navController.navigateUp()
        }
    }

    private fun prepareMediaWith(url: String)
    {
        try
        {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(url)
            mediaPlayer?.prepare()
            mediaPlayer?.start()

            setPlay()
            mediaPlayer?.setOnCompletionListener(this)
        }
        catch(e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun playSongAt(position: Int)
    {
        setSelected(currentPlaying, position)
        showProgressBar(true)
        showPlayer(true)
        mediaPlayer?.let {
            if(!it.isPlaying)
            {
                currentPlaying = position
                prepareMediaWith(songs[currentPlaying].previewUrl)
            }
            else
            {
                if(currentPlaying == position)
                {
                    showProgressBar(false)
                    setPause()
                    it.pause()
                }
                else
                {
                    setPause()
                    it.stop()
                    currentPlaying = position
                    prepareMediaWith(songs[currentPlaying].previewUrl)
                }
            }
        }
    }

    override fun onCompletion(mediaPlayer: MediaPlayer?)
    {
        //showPlayer(false)
        if(currentPlaying < songs.size - 1)
        {
            setSelected(currentPlaying, currentPlaying + 1)
            currentPlaying++
            prepareMediaWith(songs[currentPlaying].previewUrl)
        }
        else
        {
            setPause()
        }
    }

    override fun onClick(v: View?)
    {
        when(v?.id)
        {
            R.id.btnPreview ->
            {
                mediaPlayer?.let {
                    if(currentPlaying > 0)
                    {
                        if(it.isPlaying)
                        {
                            it.pause()
                        }
                        setSelected(currentPlaying, currentPlaying - 1)
                        currentPlaying--
                        prepareMediaWith(songs[currentPlaying].previewUrl)
                    }
                }
            }
            R.id.btnPlay ->
            {
                mediaPlayer?.let {
                    if(it.isPlaying && currentPlaying >= 0)
                    {
                        setPause()
                        it.pause()
                    }
                    else if(!it.isPlaying && currentPlaying >= 0)
                    {
                        setPlay()
                        it.start()
                    }
                }
            }
            R.id.btnNext ->
            {
                mediaPlayer?.let {
                    if(currentPlaying < songs.size - 1)
                    {
                        if(it.isPlaying)
                        {
                            it.pause()
                        }
                        setSelected(currentPlaying, currentPlaying + 1)
                        currentPlaying++
                        prepareMediaWith(songs[currentPlaying].previewUrl)
                    }
                }
            }
            R.id.btnStop ->
            {
                mediaPlayer?.let {
                    if(it.isPlaying)
                    {
                        setPause()
                        it.stop()
                    }
                }
            }
        }
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
